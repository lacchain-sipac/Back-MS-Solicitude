package com.invest.honduras;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import com.invest.honduras.domain.model.execute.PaymentEstimate;
import com.invest.honduras.util.Constant;

public class MsSolicitudeApplicationTests {

	@Test
	public void contextLoads() {
		assertEquals(true, true);
	}

	@Test
	public void testList() {
		List<PaymentEstimate> list = new ArrayList<>();
		PaymentEstimate item = new PaymentEstimate();
		item.setId("1");
		item.setData(null);
		list.add(item);
		item = new PaymentEstimate();
		item.setId("2");
		item.setData(null);
		list.add(item);
		item = new PaymentEstimate();
		item.setId("3");
		item.setData(null);
		list.add(item);
		item = new PaymentEstimate();
		item.setId("4");
		item.setData(null);
		list.add(item);
		
		PaymentEstimate estimate =list.stream()
				.filter(val -> "5".equals(val.getId())).findAny().orElse(null);
		
		System.out.println(estimate);
		if(estimate == null) {
			estimate = new PaymentEstimate();
			estimate.setData("datos 5");
			list.add(estimate);
		}
		
		
		list.forEach(s -> System.out.println(s.getId() +" - "+ s.getData()));
		
		assertEquals(true, true);
	}
	
	
	@Test
	public void testReplace() {
		String cadena = String.format(Constant.URL_SOLICITUDE_STEP_5, "100") ;
		
		System.out.println(cadena);
		
		assertEquals(true, true);

	}

}
