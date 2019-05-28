package com.fervort.enofer.log;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

import com.fervort.enofer.Activator;

public class Logger {

	// TODO replace with proper logging
	public static void write(String strLog)
	{
		if(Activator.getDefault().getPreferenceStore().getBoolean("com.fervort.enofer.preferencesstore.settings.enovia.enablelogs"))
		{
			try {
				String strPath = Activator.getDefault().getPreferenceStore().getString("com.fervort.enofer.preferencesstore.settings.enovia.logpath");
				if((new File(strPath)).isDirectory())
				{
						PrintWriter pw = new PrintWriter(new FileWriter(strPath+"\\EnoFerPlugin.log",true));
						pw.write("\n"+strLog);
						pw.close();
				}else
				{
					System.out.println("Directory doesn't exist "+strPath);
				}
			} catch (IOException e) {
				System.out.println("Exception "+e);
			}
		}
	}
}
