package com.hcalendar.ui;

import java.util.ArrayList;
import java.util.List;

public interface IWindowDataHanlder {

	List<IWindowDataHanlder> childWindows =  new ArrayList<IWindowDataHanlder>();
	
	public void setData();

	public void notifyDataChange();

}
