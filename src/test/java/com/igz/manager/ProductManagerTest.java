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
import com.igz.entity.category.CategoryDto;
import com.igz.entity.product.ProductDto;
import com.igz.entity.product.ProductManager;
import com.igz.entity.product.ProductDto.UnitType;
import com.igz.exception.IgzException;
import com.igz.helpers.TestHelper;
import com.igz.test.helper.ExceptionMatcher;

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
    
    @Test 
    public void TestSaveProduct() throws IgzException {
    	ProductDto product = new ProductDto();
    	product.setCategory(new CategoryDto(TestHelper.category1.getId().toString()));
    	product.setName("save product test");
    	product.setUnits(1);
    	product.setUnitType(UnitType.KG);
    	ProductDto savedProduct = manager.saveProduct(product);
    	assertNotNull("Assigned id",savedProduct.getId());
    }

    @Test 
    public void TestSaveProductInvalidCategory() throws IgzException {
    	thrown.expect(IgzException.class);
    	thrown.expect(ExceptionMatcher.hasCode(IgzException.IGZ_INVALID_PARAMS));

    	ProductDto product = new ProductDto();
    	product.setCategory(new CategoryDto());
    	product.setName("save product test");
    	product.setUnits(1);
    	product.setUnitType(UnitType.KG);
    	manager.saveProduct(product);
    }    
    
    @Test 
    public void TestSaveProductInvalidParamsNoCategory() throws IgzException {
    	thrown.expect(IgzException.class);
    	thrown.expect(ExceptionMatcher.hasCode(IgzException.IGZ_INVALID_CATEGORY));

    	ProductDto product = new ProductDto();
    	product.setCategory(new CategoryDto("-1"));
    	product.setName("save product test");
    	product.setUnits(1);
    	product.setUnitType(UnitType.KG);
    	manager.saveProduct(product);
    }
    
    @Test 
    public void TestSaveProductInvalidParamsNoCategory2() throws IgzException {
    	thrown.expect(IgzException.class);
    	thrown.expect(ExceptionMatcher.hasCode(IgzException.IGZ_INVALID_PARAMS));

    	ProductDto product = new ProductDto();
    	product.setCategory(new CategoryDto());
    	product.setName("save product test");
    	product.setUnits(1);
    	product.setUnitType(UnitType.KG);
    	manager.saveProduct(product);
    }    
    
    @Test 
    public void TestSaveProductInvalidParamsNoName() throws IgzException {
    	thrown.expect(IgzException.class);
    	thrown.expect(ExceptionMatcher.hasCode(IgzException.IGZ_INVALID_PARAMS));

    	ProductDto product = new ProductDto();
    	product.setCategory(new CategoryDto(TestHelper.category1.getId().toString()));
    	product.setUnits(1);
    	product.setUnitType(UnitType.KG);
    	manager.saveProduct(product);
    } 
    
    @Test 
    public void TestSaveProductInvalidParamsNoUnits() throws IgzException {
    	thrown.expect(IgzException.class);
    	thrown.expect(ExceptionMatcher.hasCode(IgzException.IGZ_INVALID_PARAMS));

    	ProductDto product = new ProductDto();
    	product.setCategory(new CategoryDto(TestHelper.category1.getId().toString()));
    	product.setName("save product test");
    	product.setUnitType(UnitType.KG);
    	manager.saveProduct(product);
    }     
    
    @Test 
    public void TestSaveProductInvalidParamsNoUnitType() throws IgzException {
    	thrown.expect(IgzException.class);
    	thrown.expect(ExceptionMatcher.hasCode(IgzException.IGZ_INVALID_PARAMS));

    	ProductDto product = new ProductDto();
    	product.setCategory(new CategoryDto(TestHelper.category1.getId().toString()));
    	product.setName("save product test");
    	product.setUnits(1);
    	manager.saveProduct(product);
    }         
    
    @Test 
    public void TestSaveProductDuplicate() throws IgzException {
    	thrown.expect(IgzException.class);
    	thrown.expect(ExceptionMatcher.hasCode(IgzException.IGZ_DUPLICATE_PRODUCT));
    	
    	ProductDto product = new ProductDto();
    	product.setCategory(new CategoryDto(TestHelper.category1.getId().toString()));
    	product.setName(TestHelper.product1.getName());
    	product.setUnits(1);
    	product.setUnitType(UnitType.KG);
    	ProductDto savedProduct = manager.saveProduct(product);
    }

}
