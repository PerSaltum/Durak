package main.strategy;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;

public class JarLoaderExample {

	public static void main(String[] args) {
		final Strategy strat1;
		
		try {
			strat1 = new StrategyJarLoader("C:\\strat1.jar").load();
		} catch (Exception e) {
			e.printStackTrace();
			return;
		}
		
		strat1.move(null);
	}
	
	/**
	 * Specify path to jar file.
	 * The loader will instantiate MyStrategy class from its default package. 
	 */
	private static class StrategyJarLoader {
		private final String path;

		public StrategyJarLoader(String path) {
			this.path = path;
		}
		
		public Strategy load() throws MalformedURLException, ClassNotFoundException, NoSuchMethodException, SecurityException, InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
			File strategy = new File(path);
			@SuppressWarnings("deprecation")
			URL[] url = new URL[] { strategy.toURL() };
			
			URLClassLoader child = new URLClassLoader(url, this.getClass().getClassLoader());
			Class<?> classToLoad = Class.forName("MyStrategy", true, child);
			Object instance = classToLoad.newInstance();
			return (Strategy) instance;
			
		}
	}
}
