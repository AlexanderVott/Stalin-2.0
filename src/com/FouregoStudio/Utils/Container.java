package com.FouregoStudio.Utils;

import java.util.HashMap;

public class Container {
	
	protected static HashMap<String, Object> map = null;
	
	public static void set(String key, Object value) {
		if (map == null) 
			map = new HashMap<String, Object>();
		map.put(key, value);
	}
	
	// здесь реализовано получение объекта и его автоматическое удаление, 
	// т.к. предполагается, что это будет наиболее частым требованием к контейнеру
	public static Object get(String key) {
		if (map != null) { 
			Object value = map.get(key);
			map.remove(key);
			return value;
		} else
			map = new HashMap<String, Object>();
		return null;
	}
	
	public static Object getWithoutRemove(String key) {
		if (map != null) { 
			return map.get(key);
		} else
			map = new HashMap<String, Object>();
		return null;
	}
	
	public static boolean remove(String key) {
		if (map != null) { 
			map.remove(key);
			return true;
		} else
			map = new HashMap<String, Object>();
		return false;
	}
	
	public static boolean isEntity(String key) {
		if (map == null) {
			map = new HashMap<String, Object>();
			return false;
		} else
			return (map.get(key) != null);
	}

}
