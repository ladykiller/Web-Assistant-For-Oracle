package com.test;

import org.junit.Test;

import com.ximpleware.VTDGen;

public class testVtd {

	@Test
	public void testVtd(){
		VTDGen vg = new VTDGen();
		vg.setDoc("<root><item>1</item><item>2</item><item>3</item><item>4</item></root>".getBytes());
	}
}
