package com.beust.doclipse.tag;


/**
 * This class
 *
 * @author Cedric Beust, Jul 18, 2004
 * 
 */
public class TagFragmentFactory {

  /**
   * @param pref
   * @return
   */
  public static ITagFragment newTagFragment(String pref) {
    return new TagFragmentAntlr(pref);
  }

}
