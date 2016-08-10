package com.simdezimon.jtorio.misc;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import org.luaj.vm2.*;
import org.luaj.vm2.lib.PackageLib;
import org.luaj.vm2.lib.jse.*;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import com.simdezimon.jtorio.BuildSolver;
import com.simdezimon.jtorio.LuaLoader;
import com.simdezimon.jtorio.prototypes.Prototypes;
import com.simdezimon.jtorio.prototypes.Recipe;
import com.simdezimon.jtorio.prototypes.RecipeMaterial;


/**
 * Hello world!
 *
 */
public class App {
	public static void main(String[] args) throws FileNotFoundException, IOException {
		
		Prototypes prototypes = LuaLoader.loadPrototypes("C:/Program Files/Factorio/", Lists.newArrayList());

		
		prototypes.getMachines().values().forEach(System.out::println);
		System.out.println();
		prototypes.getRecipes().values().forEach(System.out::println);
		
		BuildSolver.solve(prototypes,ImmutableMap.of(prototypes.getItems().get("processing-unit"),1.));
	
	}

}