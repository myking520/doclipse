package com.beust.doclipse;


/**
 * This class
 *
 * @author Cedric Beust, Jul 18, 2004
 * 
 */
public interface ITagFragment {
  
  public String getFragment();
  
  public String getTagName();
  
  public String getAttributeName();
  
  public boolean completesOnTagName();
  
  public boolean completesOnAttribute();

}
