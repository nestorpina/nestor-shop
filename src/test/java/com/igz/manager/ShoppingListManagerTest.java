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
import com.igz.entity.shoppinglist.ShoppingListDto;
import com.igz.entity.shoppinglist.ShoppingListManager;
import com.igz.entity.shoppinglistitem.ShoppingListItemDto;
import com.igz.exception.IgzException;
import com.igz.helpers.TestHelper;
import com.igz.test.helper.ExceptionMatcher;

@RunWith(value=BlockJUnit4ClassRunner.class)
public class ShoppingListManagerTest extends TestCase {

    private final LocalServiceTestHelper helper = new LocalServiceTestHelper(
			 new LocalDatastoreServiceTestConfig().setDefaultHighRepJobPolicyUnappliedJobPercentage(20)
    		,new LocalBlobstoreServiceTestConfig());

    private final ShoppingListManager shoppingListM = new ShoppingListManager();

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
    public void testGetByStringKey() {
    	ShoppingListDto list = createAndSaveTestList();
    	ShoppingListDto listFromDatastore = shoppingListM.getByKeyString(list.getKey().getString());
    	assertEquals(list, listFromDatastore);
    }    
    
    @Test
    public void testAddProductsInUnsavedList() throws IgzException {
    	thrown.expect(IllegalArgumentException.class);
    	
    	ShoppingListDto list = new ShoppingListDto();
   		shoppingListM.addProduct(list, TestHelper.product1);
    }

    /** 
     * We add two products, and test the shopping list size afterwards
     * 
     * @throws IgzException
     */
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
    	List<ShoppingListItemDto> itemList = shoppingListM.getShoppingListItems(list.getKey());
    	assertEquals("Item list size", TestHelper.products.size(), itemList.size());
    }

    /**
     * We add two products, and one of the previous products again, we check
     * - the shopping list size
     * - the quantity of each item ordered 
     * 
     * @throws IgzException
     */
    @Test
    public void testAddExistingProducts() throws IgzException {
    	
    	ShoppingListDto list = createAndSaveTestList();

    	for (ProductDto product : TestHelper.products) {
    		shoppingListM.addProduct(list, product);
		}
    	ProductDto firstProduct = TestHelper.product1;
		shoppingListM.addProduct(list, firstProduct);
    	
    	List<ShoppingListItemDto> itemList = shoppingListM.getShoppingListItems(list.getKey());
    	assertEquals("Item list size", TestHelper.products.size(), itemList.size());
    	for (ShoppingListItemDto item : itemList) {
    		if(item.getProduct().getId().equals(firstProduct.getId())) {
    			assertEquals("Quantity expected", 2, item.getQuantity().intValue());
    		} else {
    			assertEquals("Quantity expected", 1, item.getQuantity().intValue());
    		}
		}
    	assertEquals("Item list size", TestHelper.products.size(), itemList.size());
    }
    
    /**
     * We add two products, delete one and test the shopping list size afterwards
     * 
     * @throws IgzException
     */    
    @Test
    public void testRemoveItemFromShoppingList() {
    	ShoppingListDto list = createAndSaveTestList();

    	
    	ShoppingListItemDto addedProduct = null;
    	for (ProductDto product : TestHelper.products) {
    		addedProduct = shoppingListM.addProduct(list, product);
		}
    	
    	shoppingListM.removeProduct(list.getKey(), addedProduct.getId());
    	
    	// Retrieve again from datastore
    	List<ShoppingListItemDto> itemList2 = shoppingListM.getShoppingListItems(list.getKey());
    	assertEquals("Items in list after deletion", TestHelper.products.size()-1 , itemList2.size());
    }

    
    /**
     * We try to set the quantity of an item order, where that item doesn't exists
     * @throws IgzException 
     */
    @Test
    public void testSetQuantityOfNonExistantProduct() throws IgzException {
    	thrown.expect(IgzException.class);
    	thrown.expect(ExceptionMatcher.hasCode(IgzException.IGZ_INVALID_SHOPPING_LIST_ITEM));
    	
    	ShoppingListDto list = createAndSaveTestList();    	
    	shoppingListM.setProductQuantity(list.getKey(), TestHelper.product1.getId(), 20);
    }
    
    /**
     * We try to set the quantity of an item order, setting it to a negative number
     * @throws IgzException 
     */
    @Test
    public void testSetQuantityOfProductWithWrongParameters() throws IgzException {
    	thrown.expect(IllegalArgumentException.class);
    	
    	ShoppingListDto list = createAndSaveTestList();    	
    	shoppingListM.setProductQuantity(list.getKey(), TestHelper.product1.getId(), -1);
    }
    
    /**
     * We try to set the quantity of an item order, and we test the quantity was changed
     * @throws IgzException 
     */
    @Test
    public void testSetQuantityOfProduct() throws IgzException {
    	ShoppingListDto list = createAndSaveTestList();
    	ShoppingListItemDto item = shoppingListM.addProduct(list, TestHelper.product1);
    	shoppingListM.setProductQuantity(list.getKey(), item.getId(), 20);
    	List<ShoppingListItemDto> products = shoppingListM.getShoppingListItems(list.getKey());
    	assertEquals("Quantity of product1 ordered", 20, products.get(0).getQuantity().intValue());
    }
    
    /**
     * We try to set the quantity of an item order to 0, and we test that the item is removed
     * @throws IgzException 
     */
    @Test
    public void testSetQuantityOfProductToZero() throws IgzException {
    	ShoppingListDto list = createAndSaveTestList();
    	ShoppingListItemDto item = shoppingListM.addProduct(list, TestHelper.product1);
    	shoppingListM.setProductQuantity(list.getKey(), item.getId(), 0);
    	List<ShoppingListItemDto> products = shoppingListM.getShoppingListItems(list.getKey());
    	assertTrue("Product list size should be empty", products.isEmpty());
    }  
    
    /**
     * We try to buy a product with wrong parameters
     * @throws IgzException
     */
    @Test
    public void testBuyProductWithWrongParameters() throws IgzException {
    	thrown.expect(IllegalArgumentException.class);
    	shoppingListM.buyProduct(null, null, null);
    }

    /**
     * We try to buy a product that doesn't exist on the list 
     * @throws IgzException
     */
    @Test
    public void testBuyProductNonExistant() throws IgzException {
    	thrown.expect(IgzException.class);
    	thrown.expect(ExceptionMatcher.hasCode(IgzException.IGZ_INVALID_SHOPPING_LIST_ITEM));

    	ShoppingListDto list = createAndSaveTestList();
    	shoppingListM.buyProduct(list.getKey(), 1L, new Date());
    }

    /**
     * We try to buy a product, and we test the date was added successfully
     * @throws IgzException
     */
    @Test
    public void testBuyProduct() throws IgzException {

    	ShoppingListDto list = createAndSaveTestList();
    	ShoppingListItemDto item = shoppingListM.addProduct(list, TestHelper.product1);
    	Date dateBought = new Date();
    	shoppingListM.buyProduct(list.getKey(), item.getId(), dateBought);
    	List<ShoppingListItemDto> products = shoppingListM.getShoppingListItems(list.getKey());
    	assertEquals("Date bought set", dateBought, products.get(0).getDateBought());
    	
    }    
    
    @Test
    /**
     * We test that test user has only one list, and after we create one list
     * it has two lists. It's transactional so it must work always even when
     * UnappliedJobPercentage is high.
     * 
     * @throws IgzException
     */
    public void testGetByUser() throws InterruptedException {
    	List<ShoppingListDto> list = shoppingListM.findByUser(TestHelper.user);
    	assertEquals("shopping lists of user", 1, list.size());
    	createAndSaveTestList();
    	list = shoppingListM.findByUser(TestHelper.user);
    	assertEquals("shopping lists of user", 2, list.size());
    }
    

    /* --------------- Helper methods --------------------- */
    
    private ShoppingListDto createAndSaveTestList() {
    	final ShoppingListDto list = createTestList();
		shoppingListM.save(list);
    	return list;
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
