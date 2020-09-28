package com.fervort.enofer.preferences;

import java.util.HashMap;
import java.util.Map;

import javax.xml.crypto.dsig.spec.HMACParameterSpec;

import com.fervort.enofer.Activator;

public class EnoProperties {

	public static String getPropertyValue(String strKey)
	{
		String strProperties = Activator.getDefault().getPreferenceStore().getString("com.fervort.enofer.preferencesstore.properties");
		
		if(strProperties.contains(strKey))	
		{
			String[] aProperties = strProperties.split(",");
			for (String string : aProperties) {
				if(string.contains("="))
				{
					String[] strKeyValue = string.split("=");
					if(strKeyValue[0].trim().equalsIgnoreCase(strKey))
					{
						return strKeyValue[1];
					}
				}
				
			}
		}
		return null;
	}
}
