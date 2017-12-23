package com.tranxitpro.provider.Utilities;


import android.content.Context;
import android.graphics.Typeface;

import java.util.Hashtable;


public class Typefaces {


	private static final Hashtable<String, Typeface> cache = new Hashtable<String, Typeface>();


	public static Typeface get(Context c, String name) {

		synchronized (cache) {

			if (!cache.containsKey(name)) {

				Typeface t = Typeface.createFromAsset(c.getResources().getAssets(),	String.format("%s", name));

				cache.put(name, t);

			}

			return cache.get(name);

		}


	}

}
