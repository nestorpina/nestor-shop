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
import com.igz.entity.shoppinglist.ShoppingListDto;
import com.igz.entity.shoppinglist.ShoppingListManager;
import com.igz.entity.shoppinglistitem.ShoppingListItemDto;
import com.igz.entity.user.UserDto;
import com.igz.entity.user.UserManager;
import com.igz.exception.IgzException;
import com.igz.test.helper.TestHelper;

@RunWith(value=BlockJUnit4ClassRunner.class)
public class ShoppingListManagerTest extends TestCase {

    private final LocalServiceTestHelper helper = new LocalServiceTestHelper(
			 new LocalDatastoreServiceTestConfig().setDefaultHighRepJobPolicyUnappliedJobPercentage(1)
    		,new LocalBlobstoreServiceTestConfig());

    private final ShoppingListManager shoppinListM = new ShoppingListManager();
	private final UserManager userM = new UserManager();
	private final ProductManager productM = new ProductManager();

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
    public void testAddProductsInUnsavedList() throws IgzException {
    	thrown.expect(IllegalArgumentException.class);
    	
    	ShoppingListDto list = new ShoppingListDto();
    	ProductDto product = productM.find(1).get(0);
   		shoppinListM.addProduct(list, product);
    }

    @Test
    public void testAddProducts() throws IgzException {
    	
    	UserDto user = userM.get("nestor.pina@intelygenz.com");
    	ShoppingListDto list = createTestList(user);
    	shoppinListM.save(list);

    	List<ProductDto> products = productM.find(2);
    	for (ProductDto product : products) {
    		shoppinListM.addProduct(list, product);
		}
    	
    	List<ShoppingListItemDto> itemList = shoppinListM.getItemsFromList(list.getKey());
    	assertEquals("Item list size", 2, itemList.size());
    }
    
    @Test
    public void testAddExistingProducts() throws IgzException {
    	
    	UserDto user = userM.get("nestor.pina@intelygenz.com");
    	ShoppingListDto list = createTestList(user);
    	shoppinListM.save(list);

    	List<ProductDto> products = productM.find(2);
    	for (ProductDto product : products) {
    		shoppinListM.addProduct(list, product);
		}
    	ProductDto firstProduct = products.get(0);
		shoppinListM.addProduct(list, firstProduct);
    	
    	List<ShoppingListItemDto> itemList = shoppinListM.getItemsFromList(list.getKey());
    	assertEquals("Item list size", 2, itemList.size());
    	for (ShoppingListItemDto item : itemList) {
    		if(item.getProduct().getId().equals(firstProduct.getId())) {
    			assertEquals("Quantity expected", 2, item.getQuantity().intValue());
    		} else {
    			assertEquals("Quantity expected", 1, item.getQuantity().intValue());
    		}
		}
    	assertEquals("Item list size", 2, itemList.size());
    }

	private ShoppingListDto createTestList(UserDto user) {
		ShoppingListDto list = new ShoppingListDto();
    	list.setCreationDate(new Date());
    	list.setName("test shopping list");
    	list.setOpen(true);
    	list.setOwner(user.getKey());
		return list;
	}    
    
}
