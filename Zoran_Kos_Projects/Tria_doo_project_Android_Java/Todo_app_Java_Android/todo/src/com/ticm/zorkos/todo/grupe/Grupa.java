package com.ticm.zorkos.todo.grupe;

import java.util.ArrayList;
import java.util.List;

public class Grupa {
	  public String string;
	  public final List<String> children = new ArrayList<String>();

	  public Grupa(String string) {
	    this.string = string;
	  }
}
