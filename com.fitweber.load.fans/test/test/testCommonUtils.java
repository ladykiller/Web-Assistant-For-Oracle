package test;
import java.util.ArrayList;

import org.junit.Test;

import com.fitweber.pojo.ExecelElement;
import com.fitweber.pojo.Localtion;
import com.fitweber.util.CommonUtils;


public class testCommonUtils {

	@Test
	public void testCreateExecel(){
		ArrayList<ExecelElement> elementList  = new ArrayList<ExecelElement>();
		Localtion l = new Localtion(1,1);
		ExecelElement e =new ExecelElement();
		e.setLocaltion(l);
		e.setContent("245456");
		elementList.add(e);
		CommonUtils.createExecel("test", "E:/Dev-Cpp/", elementList);
	}
	
	@Test
	public void testReadExecel(){
		ArrayList<Localtion> localtionList = new ArrayList<Localtion>();
		int i,j;
		for(i=1;i<=3;i++){
			for(j=1;j<=3;j++){
				localtionList.add(new Localtion(i,j));
			}
		}
		ArrayList<ExecelElement> elementList = CommonUtils.readExecel("E:/Dev-Cpp/test.xls", localtionList);
		for(ExecelElement e:elementList){
			System.out.println(e.getContent());
		}
	}
}
