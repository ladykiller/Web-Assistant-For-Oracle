package com.test;

import java.io.ByteArrayOutputStream;
import java.util.HashMap;

import org.junit.Test;

import com.ximpleware.AutoPilot;
import com.ximpleware.VTDGen;
import com.ximpleware.VTDNav;
import com.ximpleware.XMLModifier;

public class testVtd {
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Test
	public void testOverWrite() throws Exception{
		VTDGen vg = new VTDGen();
		vg.setDoc("<root><item>1</item><item>2</item><item>3</item><item>4</item></root>".getBytes("UTF-8"));
		HashMap m = new HashMap();
		m.put("1", "A");
		m.put("2", "B");
		m.put("3", "C");
		m.put("4", "D");
		
		vg.parse(true);
		VTDNav vn = vg.getNav();
		AutoPilot ap = new AutoPilot(vn);
		XMLModifier xm = new XMLModifier(vn);
		ap.selectXPath("//item");
		while(ap.evalXPath() > -1){
			System.out.println(vn.toString(vn.getText()));
			xm.updateToken(vn.getText(), (String)m.get(vn.toString(vn.getText())));
		}
		ByteArrayOutputStream os = new ByteArrayOutputStream();
		xm.output(os);
		System.out.println(os.toString("UTF-8"));
		os.flush();
		os.close();
	}
}
