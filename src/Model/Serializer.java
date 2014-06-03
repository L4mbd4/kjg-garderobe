package Model;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.LinkedList;
import java.util.List;

import android.annotation.SuppressLint;
import android.content.Context;

public class Serializer {

	public static void serializeChecklist(List<ChecklistItem> checklist,
			Context context) {

		Checklist clw = new Checklist(checklist);
		try {
			FileOutputStream fos = context.openFileOutput("checklist.cl",
					Context.MODE_PRIVATE);
			ObjectOutputStream os = new ObjectOutputStream(fos);
			os.writeObject(clw);
			os.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static List<ChecklistItem> deserializeChecklist(Context context) {
		Checklist c = null;
		File dir = context.getFilesDir();

		FilenameFilter filter = new FilenameFilter() {
			@SuppressLint("DefaultLocale")
			@Override
			public boolean accept(File dir, String name) {
				String lowercaseName = name.toLowerCase();
				if (lowercaseName.endsWith(".cl")) {
					return true;
				} else {
					return false;
				}
			}
		};

		for (File f : dir.listFiles(filter)) {
			try {
				FileInputStream fis = context.openFileInput(f.getName());
				ObjectInputStream is = new ObjectInputStream(fis);
				c = (Checklist) is.readObject();
				is.close();
			} catch (IOException e) {
				e.printStackTrace();
			} catch (ClassNotFoundException e) {
				e.printStackTrace();

			}
		}

		if (c != null) {
			return c.getList();
		} else {
			return null;
		}
	}

	public static void serializeParty(Party p, String fileName, Context context) {
		try {
			FileOutputStream fos = context.openFileOutput(fileName,
					Context.MODE_PRIVATE);
			ObjectOutputStream os = new ObjectOutputStream(fos);
			os.writeObject(p);
			os.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void serializeParty(Party p, Context context) {
		serializeParty(p, p.getName() + ".p", context);
	}

	public static Party[] getPartys(Context context) {
		LinkedList<Party> pl = new LinkedList<Party>();
		File dir = context.getFilesDir();

		FilenameFilter filter = new FilenameFilter() {
			@SuppressLint("DefaultLocale")
			@Override
			public boolean accept(File dir, String name) {
				String lowercaseName = name.toLowerCase();
				if (lowercaseName.endsWith(".p")) {
					return true;
				} else {
					return false;
				}
			}
		};

		for (File f : dir.listFiles(filter)) {
			if (f.isFile()) {
				pl.add(deserializeParty(f.getName(), context));
			}
		}

		return pl.toArray(new Party[0]);
	}

	public static Party deserializeParty(String file, Context c) {
		Party p = null;
		try {
			FileInputStream fis = c.openFileInput(file);
			ObjectInputStream is = new ObjectInputStream(fis);
			p = (Party) is.readObject();
			is.close();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();

		}
		return p;
	}
}
