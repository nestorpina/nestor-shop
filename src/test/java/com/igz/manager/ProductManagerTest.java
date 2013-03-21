package com.igz.manager;

import java.util.Date;
import java.util.List;

import junit.framework.TestCase;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.junit.runners.BlockJUnit4ClassRunner;

import com.google.appengine.tools.development.testing.LocalBlobstoreServiceTestConfig;
import com.google.appengine.tools.development.testing.LocalDatastoreServiceTestConfig;
import com.google.appengine.tools.development.testing.LocalServiceTestHelper;
import com.igz.entity.product.ProductDto;
import com.igz.entity.product.ProductManager;
import com.igz.exception.IgzException;
import com.igz.helpers.TestHelper;

@RunWith(value=BlockJUnit4ClassRunner.class)
public class ProductManagerTest extends TestCase {

    private final LocalServiceTestHelper helper = new LocalServiceTestHelper(
			 new LocalDatastoreServiceTestConfig().setDefaultHighRepJobPolicyUnappliedJobPercentage(1)
    		,new LocalBlobstoreServiceTestConfig());

    private final ProductManager manager = new ProductManager();

    @Rule
    public ExpectedException thrown = ExpectedException.none();  
    
    @Override
    @Before
    public void setUp() {
        this.helper.setUp();
        TestHelper.prepareDS();
    }

    @Override
    @After
    public void tearDown() {
        this.helper.tearDown();
    }

    @Test
    public void testSave() throws IgzException {
    	ProductDto p = new ProductDto();
    	p.setCreationDate(new Date());
    	p.setName("Milk");
    	p.setDescription("Good for your bones");
    	p.setUnitType(ProductDto.UnitType.LITER);
    	p.setUnits(1);
    	manager.save(p);
    	
    	assertNotNull("id not filled", p.getId());
    	ProductDto productFromDB = manager.get(p.getId());
    	
    	assertNotSame(p, productFromDB);
    	
    }
    
    @Test
    public void testFindAllProducts() {
    	List<ProductDto> products = manager.findAll();
    	assertEquals("all products", TestHelper.products.size(), products.size());
    }
    
    @Test 
    public void testFindProductsByCategory() {
    	List<ProductDto> foods = manager.findByCategory(TestHelper.category1);
    	assertEquals("number of food products", 4, foods.size());
    	List<ProductDto> gadgets = manager.findByCategory(TestHelper.category2);
    	assertEquals("number of gadgets products", 2, gadgets.size());
    }
    
}
