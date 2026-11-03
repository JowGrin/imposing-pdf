package io.github.jowgrin.imposingpdf.environment;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

public class UserSetting implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private PageSizesArray pageSizes;
	private UnitsArray units;
	private File recentlyPathOpen;
	
	private final String USER_SETTING_FILENAME = "UserSetting.dll";
	
	
	public UserSetting() throws FileNotFoundException, IOException{
		File file = new File(USER_SETTING_FILENAME);
		UserSetting userSetting = null;
		try {
			if (file.exists()) {
				userSetting = loadUserSetting();			
			}
			else {
				userSetting = getDefaultUserSetting();
			}
		} catch(IOException | ClassNotFoundException e) {
			e.printStackTrace();
			userSetting = getDefaultUserSetting();
		} finally {
			this.pageSizes = userSetting.pageSizes;
			this.units = userSetting.units;
			this.recentlyPathOpen = userSetting.recentlyPathOpen;	
			storeUserSetting();			
		}
	}
	
	private UserSetting(PageSizesArray pageSizes, UnitsArray units, File recentlyPathOpen) {
		this.pageSizes = pageSizes;
		this.units = units;
		this.recentlyPathOpen = recentlyPathOpen;
	}
	
	public PageSizesArray getPageSizes() {
		return pageSizes;
	}
	
	public UnitsArray getUnits() {
		return units;
	}
	
	public File getRecentlyPathOpen() {
		return recentlyPathOpen;
	}
	
	public void updateRecentlyPath(File path) {
		if (path.exists())
			this.recentlyPathOpen = path;
		updateSettingUserBackup();
	}
	
	public void updateSettingUserBackup() {
		try {
			storeUserSetting();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private UserSetting loadUserSetting() throws FileNotFoundException, IOException, ClassNotFoundException {
		UserSetting userSetting;
		ObjectInputStream input = new ObjectInputStream(new FileInputStream(USER_SETTING_FILENAME));
		userSetting = (UserSetting)input.readObject();
		input.close();
		return userSetting;
	}
	
	private void storeUserSetting() throws FileNotFoundException, IOException {
		ObjectOutputStream output = new ObjectOutputStream(new FileOutputStream(USER_SETTING_FILENAME));
		output.writeObject(this);
		output.flush();
		output.close();
	}
	
	private UserSetting getDefaultUserSetting() {
		PageSizesArray pageSizes = PageSizesArray.getDefault();
		UnitsArray units = UnitsArray.getDefault();
		File recentlyPathOpen = new File(".");
		return new UserSetting(pageSizes, units, recentlyPathOpen);	
	}
}
