package lxz.tutorial.mysql.domain;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class BaseDomain {

	private HashMap<String, Object> context;

	private void init() {
		context = new HashMap<>();
	}

	public final HashMap<String, Object> getContext() {
		return context;
	}

	public void setContextProperty(String key, Object value) {
		if (context == null) {
			init();
		}
		context.put(key, value);
	}

	public <T> T getContextProperty(String key) {
		if (context == null) {
			init();
		}
		return (T) context.get(key);
	}

	public void setContext(HashMap<String, Object> context) {
		this.context = context;
	}

	public <T> T generateModelExample(Class<T> clazz) {
		return generateModelExample(clazz, null, null);
	}

	public <T> T generateModelExample(Class<T> clazz, Integer offset, Integer limit) {
		try {
			Constructor<T> cons = clazz.getConstructor();
			T t = cons.newInstance();
			if (offset != null) {
				Method setOffset = clazz.getMethod("setOffset", Integer.class);
				setOffset.invoke(t, offset);
			}
			if (limit != null) {
				Method setLimit = clazz.getMethod("setLimit", Integer.class);
				setLimit.invoke(t, limit);
			}
			Method m = clazz.getMethod("createCriteria");
			Object criteria = m.invoke(t);
			Class criteriaClass = Class.forName(clazz.getName() + "$Criteria");
			if (context != null && !context.isEmpty()) {
				for (String key : context.keySet()) {
					if (context.get(key) != null) {
						if(context.get(key) instanceof String) {
							if(((String)context.get(key)).isEmpty()) {
								continue;
							}
						}
						Method criteriaMethod = null;
						Object paramValue = null;
						if (key.split("_").length == 1) {
							criteriaMethod = findMethodByName(criteriaClass,
									"and" + Character.toUpperCase(key.charAt(0)) + key.substring(1) + "EqualTo");
							paramValue = context.get(key);
						} else if (key.split("_").length == 2 && key.endsWith("_like")) {
							String[] keyArray = key.split("_");
							criteriaMethod = findMethodByName(criteriaClass,
									"and" + Character.toUpperCase(keyArray[0].charAt(0)) + keyArray[0].substring(1)
											+ Character.toUpperCase(keyArray[1].charAt(0)) + keyArray[1].substring(1));
							paramValue = "%" + context.get(key) + "%";
						}
						if (criteriaMethod.getParameterTypes() != null) {
							Class parameterType = criteriaMethod.getParameterTypes()[0];
							criteriaMethod.invoke(criteria, parameterType.cast(paramValue));
						}
					}
				}
			}
			return t;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	private <T> Method findMethodByName(Class<T> clazz, String methodName) {
		List<Method> methods = new ArrayList<>();
		for (Method m : clazz.getMethods()) {
			if (m.getName().equals(methodName)) {
				methods.add(m);
			}
		}
		if (methods.size() == 1) {
			return methods.get(0);
		}
		throw new RuntimeException("method not found, or method name conflicts");
	}

}
