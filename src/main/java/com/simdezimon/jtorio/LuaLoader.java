package com.simdezimon.jtorio;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import org.luaj.vm2.LoadState;
import org.luaj.vm2.LuaBoolean;
import org.luaj.vm2.LuaNil;
import org.luaj.vm2.LuaNumber;
import org.luaj.vm2.LuaString;
import org.luaj.vm2.LuaTable;
import org.luaj.vm2.LuaValue;
import org.luaj.vm2.lib.jse.JsePlatform;

import com.google.common.collect.Sets;
import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonNull;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import com.simdezimon.jtorio.misc.FileUtils;
import com.simdezimon.jtorio.prototypes.Item;
import com.simdezimon.jtorio.prototypes.Machine;
import com.simdezimon.jtorio.prototypes.Prototypes;
import com.simdezimon.jtorio.prototypes.Recipe;
import com.simdezimon.jtorio.prototypes.RecipeMaterial;
import com.simdezimon.jtorio.prototypes.Resource;

public class LuaLoader {

	private LuaLoader() {

	}

	public static Prototypes loadPrototypes(
			String factorioDirectory , List<String> modDirectories) throws IOException {

		LuaValue globals = JsePlatform.standardGlobals();

		LoadState.load(new FileInputStream("require.lua"), "require", globals).call();
		File dir = new File(factorioDirectory);
		File libFile = new File(dir, "data/core/lualib");
		globals.get("package").set("path", libFile.getAbsolutePath() + "/?.lua");
		String[] libs = new String[] { "util", "dataloader", "autoplace_utils", "story" };
		for (String libname : libs) {
			File file = new File(libFile, libname + ".lua");
			LoadState.load(new FileInputStream(file), libname, globals).call(LuaString.valueOf(libname));
		}
		File baseFile = new File(dir, "data/base");
		globals.get("package").set("path",
				globals.get("package").get("path") + ";" + baseFile.getAbsolutePath() + "/?.lua");
		File data = new File(baseFile, "data" + ".lua");
		LoadState.load(new FileInputStream(data), "data", globals).call(LuaString.valueOf("data"));

		
		Set<String> important = Sets.newHashSet("table","math","debug","package","_G","python","string","os","coroutine","bit32","util","autoplace_utils");
		for(String mod : modDirectories){
			LuaTable packages = (LuaTable) globals.get("package").get("loaded");
			for(LuaValue pack : packages.keys()){
				if(!important.contains(pack.toString())){
					packages.set(pack, LuaNil.NIL);
				}
			}
			File modFile = new File(mod);
			globals.get("package").set("path", modFile.getAbsolutePath() + "/?.lua");
			File modData = new File(modFile, "data" + ".lua");
			LoadState.load(new FileInputStream(modData), "data", globals).call(LuaString.valueOf("data"));
		}
		LuaTable table = (LuaTable) globals.get("data").get("raw"); 
		
		return create(table);
	}

	private static Prototypes create(LuaTable table) {

		final Gson inner = new GsonBuilder()
				.setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
				.registerTypeAdapter(JsonRecipeMaterial.class, new JsonDeserializer<JsonRecipeMaterial>() {
					@Override
					public JsonRecipeMaterial deserialize(JsonElement json, Type typeOfT,
							JsonDeserializationContext context) throws JsonParseException {
						JsonRecipeMaterial material = new JsonRecipeMaterial();

						if (json.isJsonArray()) {
							material.setName(json.getAsJsonArray().get(0).getAsString());
							material.setAmount(json.getAsJsonArray().get(1).getAsInt());
						} else if (json.isJsonObject()) {
							JsonObject object = json.getAsJsonObject();
							material.setName(object.get("name").getAsString());
							if (object.has("type")) {
								material.setType(object.get("name").getAsString());
							}
							if (object.has("amount")) {
								material.setAmount(object.get("amount").getAsDouble());
							}
						} else {
							material.setName(json.getAsString());
							System.err.println(json.getAsString());
						}
						return material;
					}
				}).create();

		final Gson gson = new GsonBuilder()
				.setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
				.registerTypeAdapter(JsonPrototypes.class, new JsonDeserializer<JsonPrototypes>() {
					@Override
					public JsonPrototypes deserialize(JsonElement json, Type typeOfT,
							JsonDeserializationContext context) throws JsonParseException {
						JsonPrototypes prototypes = new JsonPrototypes();
						JsonObject jTypes = (JsonObject) json;
						for (Entry<String, JsonElement> entry : jTypes.entrySet()) {
							String type = entry.getKey();
							JsonObject jList = (JsonObject) entry.getValue();
							for (Entry<String, JsonElement> prototypeEntry : jList.entrySet()) {
								if (itemTypes.contains(type)) {
									JsonItem item = inner.fromJson(prototypeEntry.getValue(), JsonItem.class);
									prototypes.addItem(item);
								}else if(machineTypes.contains(type)) {
									JsonMachine machine = inner.fromJson(prototypeEntry.getValue(), JsonMachine.class);
									prototypes.addMachine(machine);
								} else if ("recipe".equals(type)) {
									JsonRecipe recipe = inner.fromJson(prototypeEntry.getValue(), JsonRecipe.class);
									prototypes.addRecipe(recipe);
								} else if ("resource".equals(type)) {
									prototypes.addResources(inner.fromJson(prototypeEntry.getValue(), JsonResource.class));
								} else if ("fluid".equals(type)) {
									prototypes.addFluid(inner.fromJson(prototypeEntry.getValue(), JsonFluid.class));
								}
							}
						}
						return prototypes;

					}
				}).setPrettyPrinting().create();

		JsonElement jsonTable = toJson(table);
		
		try {
			FileUtils.saveFile("table.json", gson.toJson(jsonTable));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		JsonPrototypes jsonPrototypes = gson.fromJson(jsonTable, JsonPrototypes.class);

		Prototypes prototypes = new Prototypes();
		Map<String, Item> items = prototypes.getItems();
		for (JsonItem jsonItem : jsonPrototypes.getItems()) {
			items.put(jsonItem.getName(), new Item(jsonItem.getName(), jsonItem.getType()));
		}
		for (JsonFluid jsonFluid : jsonPrototypes.getFluids()) {
			items.put(jsonFluid.getName(), new Item(jsonFluid.getName(), "fluid"));
			items.put("hot-"+jsonFluid.getName(), new Item("hot-"+jsonFluid.getName(), "fluid"));
		}

		Map<String, Recipe> recipes = prototypes.getRecipes();
		for (JsonRecipe jsonRecipe : jsonPrototypes.getRecipes()) {
			Recipe recipe = new Recipe(jsonRecipe.getName(), jsonRecipe.getEnergyRequired(),jsonRecipe.getCategory(),"recipe");
			for (JsonRecipeMaterial rm : jsonRecipe.getIngredients()) {
				Item item = items.get(rm.getName());
				if (item == null) {
					throw new NullPointerException(
							"Item " + rm.getName() + " for recipe " + recipe.getName() + " not found.");
				}
				recipe.getIngredients().add(new RecipeMaterial(item, rm.getAmount()));
			}
			for (JsonRecipeMaterial rm : jsonRecipe.getResults()) {
				Item item = items.get(rm.getName());
				if (item == null) {
					throw new NullPointerException(
							"Item " + rm.getName() + " for recipe " + recipe.getName() + " not found.");
				}
				recipe.getResults().add(new RecipeMaterial(item, rm.getAmount()));
			}
			recipes.put(jsonRecipe.getName(), recipe);
		}

		for (JsonResource jr : jsonPrototypes.getResources()) {
			Resource resource = new Resource(jr.getName(),
					jr.getMinable().getHardness(),jr.getMinable().getMiningTime(),jr.getCategory());
			for (JsonRecipeMaterial rm : jr.getMinable().getResults()) {
				Item item = items.get(rm.getName());
				if (item == null) {
					throw new NullPointerException(
							"Item " + rm.getName() + " for recipe " + resource.getName() + " not found.");
				}
				resource.getResults().add(new RecipeMaterial(item, rm.getAmount()));
			}
			prototypes.addRecipe(resource);
		}
		for(JsonMachine jm : jsonPrototypes.getMachines()){
			double energyUsage = jm.getEnergyUsage() * (jm.getEnergySource().isOutput() ? -1 : 1);
			Machine machine = new Machine(jm.getName(), jm.getType(), jm.getCategories()
					,recipeTypes.get(jm.getType()), jm.getCraftingSpeed(),energyUsage,jm.getEnergySource().getType(),
					jm.getEnergySource().getEmissions()*jm.getEnergyUsage());
			prototypes.addMachine(machine);
		}
		
		
		
		Item electricity = new Item("electric-power", "electric-power");
		Item burner = new Item("burner-power", "burner-power");
		Item pollution = new Item("pollution", "pollution");

		for (JsonFluid jsonFluid : jsonPrototypes.getFluids()) {
			Item fluid = items.get(jsonFluid.getName());
			Item hotFluid = items.get("hot-"+jsonFluid.getName());
			double energy = (jsonFluid.getMaxTemperature()-jsonFluid.getDefaultTemperature())*jsonFluid.getHeatCapacity();
			Recipe heatFluid = new Recipe("hot-" + fluid.getName(),energy / 1000.,null,"boiler");
			heatFluid.getIngredients().add(new RecipeMaterial(fluid, 1));
			heatFluid.getResults().add(new RecipeMaterial(hotFluid, 1));
			recipes.put(heatFluid.getName(), heatFluid);
			
			Recipe energyFluid = new Recipe("generator-" + fluid.getName(),1,null,"generator");
			energyFluid.getIngredients().add(new RecipeMaterial(hotFluid, 1));
			energyFluid.getResults().add(new RecipeMaterial(electricity, energy));
			recipes.put(energyFluid.getName(), energyFluid);
			

			Recipe offshoreFluid = new Recipe("offshore-pump-"+fluid.getName(),1,fluid.getName(),"offshore-pump");
			offshoreFluid.getResults().add(new RecipeMaterial(fluid, 1));
			prototypes.addRecipe(offshoreFluid);
		}
				
		prototypes.addItem(electricity);
		prototypes.addItem(burner);
		prototypes.addItem(pollution);
		


		Machine environment = new Machine("environment", "environment", Sets.newHashSet(""),"environment", 1., 0., "electric", 0.);
		prototypes.addMachine(environment);
		
		for (JsonItem jsonItem : jsonPrototypes.getItems()) {
			Item item = items.get(jsonItem.getName());
			Double fuelValue = jsonItem.getFuelValue();
			if(fuelValue != null){
				Recipe fuelRecipe = new Recipe("fuel-"+item.getName(), 1,"", "environment");
				fuelRecipe.getIngredients().add(new RecipeMaterial(item, 1));
				fuelRecipe.getResults().add(new RecipeMaterial(burner, fuelValue*1000.));
				prototypes.addRecipe(fuelRecipe);
				
			}
		}
		
		
		Recipe pollutionRecipe = new Recipe("pollution", 1,"", "environment");
		pollutionRecipe.getResults().add(new RecipeMaterial(pollution, 1));
		prototypes.addRecipe(pollutionRecipe);
		
		return prototypes;

	}


	private static final Set<String> itemTypes = Sets.newHashSet("item", "ammo", "armor", "capsule", "night-vision-equipment",
			"energy-shield-equipment", "battery-equipment", "solar-panel-equipment", "generator-equipment",
			"active-defense-equipment", "movement-bonus-equipment", "gun", "mining-tool", "module", 
			"deconstruction-item", "tool", "blueprint", "repair-tool","blueprint-book","rail-planner");
	
	private static final Set<String> machineTypes = Sets.newHashSet("assembling-machine","furnace","mining-drill","offshore-pump","rocket-silo","boiler","generator");
	

private static final Map<String,String> recipeTypes = new HashMap<>();
static {
	recipeTypes.put("assembling-machine", "recipe");
	recipeTypes.put("furnace", "recipe");
	recipeTypes.put("mining-drill", "resource");
	recipeTypes.put("offshore-pump", "offshore-pump");
	recipeTypes.put("rocket-silo", "recipe");
	recipeTypes.put("boiler", "boiler");
	recipeTypes.put("generator", "generator");
}

	private static JsonElement toJson(LuaValue value) {
		if (value instanceof LuaTable) {
			LuaTable table = (LuaTable) value;
			asarray: {
				JsonArray jsonArray = new JsonArray();
				int k = 1;
				for (LuaValue key : table.keys()) {
					if (!key.isint() && key.toint() != k) {
						break asarray;
					}
					k++;
					jsonArray.add(toJson(table.get(key)));
				}
				return jsonArray;
			}
			JsonObject jsonTable = new JsonObject();
			for (LuaValue key : table.keys()) {
				jsonTable.add(key.toString(), toJson(table.get(key)));
			}
			return jsonTable;
		} else if (value instanceof LuaNumber) {
			return new JsonPrimitive(value.todouble());
		} else if (value instanceof LuaBoolean) {
			return new JsonPrimitive(value.toboolean());
		} else if (value instanceof LuaNil) {
			return JsonNull.INSTANCE;
		}
		return new JsonPrimitive(value.toString());
	}

}
