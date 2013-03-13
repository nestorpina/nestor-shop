package com.igz.manager;

import static com.googlecode.objectify.ObjectifyService.ofy;

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
import com.googlecode.objectify.VoidWork;
import com.igz.entity.product.ProductDto;
import com.igz.entity.product.ProductManager;
import com.igz.entity.shoppinglist.ShoppingListDto;
import com.igz.entity.shoppinglist.ShoppingListManager;
import com.igz.entity.shoppinglistitem.ShoppingListItemDto;
import com.igz.exception.IgzException;
import com.igz.test.helper.TestHelper;

@RunWith(value=BlockJUnit4ClassRunner.class)
public class ShoppingListManagerTest extends TestCase {

    private final LocalServiceTestHelper helper = new LocalServiceTestHelper(
			 new LocalDatastoreServiceTestConfig().setDefaultHighRepJobPolicyUnappliedJobPercentage(20)
    		,new LocalBlobstoreServiceTestConfig());

    private final ShoppingListManager shoppingListM = new ShoppingListManager();
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
   		shoppingListM.addProduct(list, product);
    }

    @Test
    public void testAddProducts() throws IgzException {

    	final ShoppingListDto list = createTestList();
    	
    	ofy().transact(new VoidWork() {
			
			@Override
			public void vrun() {
				
				shoppingListM.save(list);
				
				for (ProductDto product : TestHelper.products) {
					shoppingListM.addProduct(list, product);
				}
				
			}
		});
    	List<ShoppingListItemDto> itemList = shoppingListM.getShoppingListItems(list.getId());
    	assertEquals("Item list size", 2, itemList.size());
    }
    
    @Test
    public void testAddExistingProducts() throws IgzException {
    	
    	ShoppingListDto list = createTestList();
    	shoppingListM.save(list);

    	for (ProductDto product : TestHelper.products) {
    		shoppingListM.addProduct(list, product);
		}
    	ProductDto firstProduct = TestHelper.product1;
		shoppingListM.addProduct(list, firstProduct);
    	
    	List<ShoppingListItemDto> itemList = shoppingListM.getShoppingListItems(list.getId());
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
    
    @Test
    public void removeItemFromShoppingList() {
    	ShoppingListDto list = createTestList();
    	shoppingListM.save(list);

    	
    	ShoppingListItemDto addedProduct = null;
    	for (ProductDto product : TestHelper.products) {
    		addedProduct = shoppingListM.addProduct(list, product);
		}
    	
    	shoppingListM.removeProduct(list.getId(), addedProduct.getId());
    	
    	// Retrieve again from datastore
    	List<ShoppingListItemDto> itemList2 = shoppingListM.getShoppingListItems(list.getId());
    	assertEquals("Items in list after deletion", 1, itemList2.size());
    }

	private ShoppingListDto createTestList() {
		ShoppingListDto list = new ShoppingListDto();
    	list.setCreationDate(new Date());
    	list.setName("test shopping list");
    	list.setOpen(true);
    	list.setOwner(TestHelper.user.getKey());
		return list;
	}    
    
}
