local old_require = require
function require (module)
    local ok, m = pcall (old_require, module)
     if ok then
        return m
     else 
      print(m)
     end
end
