//package com.beust.doclipse.preferences;
//
//import java.util.HashMap;
//import java.util.Iterator;
//import java.util.Map;
//import java.util.StringTokenizer;
//
//import org.eclipse.jface.preference.IPreferenceStore;
//
//import com.beust.doclipse.DoclipsePlugin;
//
//
///**
// * This class is a front-end to load and store preferences.
// * 
// * @author Cedric Beust, Jun 16, 2004
// * 
// */
//public class Preferences {
//
//  //
//  // Preference keys
//  //
//  
//   // Where the external files are stored
//  private  static final String EXTERNAL_DIRECTORY = "PreferenceDirectory";
//  // Where the checked internal directories are stored
//  private  static final String INTERNAL_CHECKED_FILES= "InternalCheckedFiles";
//  // Where the checked external  directories are stored
//  private  static final String EXTERNAL_CHECKED_FILES= "ExternalCheckedFiles";
//  // Whether we surround values with tdouble quotes
//  private static final String SURROUND_WITH_DOUBLE_QUOTES = "SurroundWithDoubleQuotes";
//  // Whether we put spaces around equal signes
//  private static final String SPACES_AROUND_EQUAL_SIGNS = "SpacesAroundEqualSigns";
//  
//  private static final String ALL = "<all>";
//
//  private  static IPreferenceStore getPrefs() {
//    return DoclipsePlugin.getDefault().getPreferenceStore();    
//  }
//  
//  /**
//   * Initialize all the default values for our plug-in
//   *
//   */
//  public static void initializeDefaultPreferences(IPreferenceStore prefs) {
//    String checkedFiles = "ejbgen.xml hibernate.xml";
//    String[] checkedFilesArray= {
//    "ejbgen.xml",
//    "hibernate.xml",
//  };
//    prefs.setDefault(SPACES_AROUND_EQUAL_SIGNS, "True");
//    prefs.setDefault(SURROUND_WITH_DOUBLE_QUOTES, "True");
//    prefs.setDefault(INTERNAL_CHECKED_FILES, checkedFiles);
//    Map internalFiles = new HashMap();
//    for (int i = 0; i < checkedFilesArray.length; i++) {
//      internalFiles.put(checkedFilesArray[i], checkedFilesArray[i]);
//    }
//    saveInternalCheckedFiles(internalFiles);
//    saveExternalCheckedFiles(null);
//  }
//
// /**
//   * @return The external directory from the IPreferenceStore
//   */
//  public static String getExternalDirectory() {
//    IPreferenceStore store = getPrefs();
//    return store.getString(Preferences.EXTERNAL_DIRECTORY);           
//  }
//  
//  /**
//   * @param text
//   */
//  public static void saveExternalDirectory(String dir) {
//    getPrefs().setValue(Preferences.EXTERNAL_DIRECTORY, dir);
//  }  
//
//  /**
//   * @return a Map(String fileName, String fileName) of the files
//   * from the IPreferenceStore
//   */
//  static public Map getExternalCheckedFiles() {
//    return getCheckedFiles(EXTERNAL_CHECKED_FILES);
//  }
//  
//  static public Map getInternalCheckedFiles() {
//    return getCheckedFiles(INTERNAL_CHECKED_FILES);
//  }
//  
//  /**
//   * 
//   * @return Map<String fileName, String fileName>
//   */
//  static private Map getCheckedFiles(String key) {
//    String strResult = (String) getPrefs().getString(key);
////    ppp("READING CHECKED FILES FOR " + key + " = " + strResult);
//    Map result = null;
//
//    if (! ALL.equals(strResult)) {
//      result = new HashMap();
//      StringTokenizer st = new StringTokenizer(strResult);
//      while (st.hasMoreElements()) {
//        String f = st.nextToken();
//        result.put(f, f);
//      }
//    }
//    
//    return result;
//  }
//
//  /**
//   * Store the checked files into the IPreferenceStore
//   */
//  static public void saveExternalCheckedFiles(Map files) {
//    saveCheckedFiles(files, EXTERNAL_CHECKED_FILES);
//  }
//  
//  static public void saveInternalCheckedFiles(Map files) {
//    saveCheckedFiles(files, INTERNAL_CHECKED_FILES);
//  }
//  
//  static private void saveCheckedFiles(Map files, String key) {
//    StringBuffer sb = new StringBuffer();
//    
//    if (null != files) {
//      for (Iterator it = files.values().iterator(); it.hasNext(); ) {
//        sb.append(it.next()).append(" ");
//      }
//    }
//    else {
//      sb.append(ALL);
//    }
//    
////    ppp("SAVING CHECKED FILES " + key + " " + sb.toString());
//    getPrefs().setValue(key, sb.toString());
//  }
//
//  private static void ppp(String s) {
//    System.out.println("[Preferences] " + s);
//  }
//
//  public static boolean surroundWithDoubleQuotes() {
//    IPreferenceStore store = getPrefs();
//    boolean result = store.getBoolean(Preferences.SURROUND_WITH_DOUBLE_QUOTES);
//    return result;
//  }
//  
//  /**
//   * @param selection
//   */
//  public static void saveSurroundWithDoubleQuotes(boolean f) {
//    getPrefs().setValue(Preferences.SURROUND_WITH_DOUBLE_QUOTES, f);
//  }
//  
//  public static boolean insertSpacesAroundEqual() {
//    IPreferenceStore store = getPrefs();
//    boolean result = store.getBoolean(Preferences.SPACES_AROUND_EQUAL_SIGNS);
//    return result;
//  }
//  
//  public static void saveInsertSpacesAroundEqual(boolean f) {
//    getPrefs().setValue(Preferences.SPACES_AROUND_EQUAL_SIGNS, f);    
//  }
//
//}
