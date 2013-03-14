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
import com.igz.test.helper.ExceptionMatcher;
import com.igz.test.helper.TestHelper;

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
    	List<ShoppingListItemDto> itemList = shoppingListM.getShoppingListItems(list.getId());
    	assertEquals("Item list size", 2, itemList.size());
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
    	
    	shoppingListM.removeProduct(list.getId(), addedProduct.getId());
    	
    	// Retrieve again from datastore
    	List<ShoppingListItemDto> itemList2 = shoppingListM.getShoppingListItems(list.getId());
    	assertEquals("Items in list after deletion", 1, itemList2.size());
    }

    
    /**
     * We try to set the quantity of an item order, where that item doesn't exists
     * @throws IgzException 
     */
    @Test
    public void testSetQuantityOfNonExistantProduct() throws IgzException {
    	thrown.expect(IgzException.class);
    	thrown.expect(ExceptionMatcher.hasCode(IgzException.IGZ_SHOPPING_LIST_ITEM_NOT_FOUND));
    	
    	ShoppingListDto list = createAndSaveTestList();    	
    	shoppingListM.setProductQuantity(list.getId(), TestHelper.product1.getId(), 20);
    }
    
    /**
     * We try to set the quantity of an item order, setting it to a negative number
     * @throws IgzException 
     */
    @Test
    public void testSetQuantityOfProductWithWrongParameters() throws IgzException {
    	thrown.expect(IllegalArgumentException.class);
    	
    	ShoppingListDto list = createAndSaveTestList();    	
    	shoppingListM.setProductQuantity(list.getId(), TestHelper.product1.getId(), -1);
    }
    
    /**
     * We try to set the quantity of an item order, and we test the quantity was changed
     * @throws IgzException 
     */
    @Test
    public void testSetQuantityOfProduct() throws IgzException {
    	ShoppingListDto list = createAndSaveTestList();
    	ShoppingListItemDto item = shoppingListM.addProduct(list, TestHelper.product1);
    	shoppingListM.setProductQuantity(list.getId(), item.getId(), 20);
    	List<ShoppingListItemDto> products = shoppingListM.getShoppingListItems(list.getId());
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
    	shoppingListM.setProductQuantity(list.getId(), item.getId(), 0);
    	List<ShoppingListItemDto> products = shoppingListM.getShoppingListItems(list.getId());
    	assertEquals("Product list size should be empty", true, products.isEmpty());
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
    	thrown.expect(ExceptionMatcher.hasCode(IgzException.IGZ_SHOPPING_LIST_ITEM_NOT_FOUND));

    	ShoppingListDto list = createAndSaveTestList();
    	shoppingListM.buyProduct(list.getId(), 1L, new Date());
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
    	shoppingListM.buyProduct(list.getId(), item.getId(), dateBought);
    	List<ShoppingListItemDto> products = shoppingListM.getShoppingListItems(list.getId());
    	assertEquals("Date bought set", dateBought, products.get(0).getDateBought());
    	
    }    
    

    /* --------------- Helper methods --------------------- */
    
    private ShoppingListDto createAndSaveTestList() {
    	ShoppingListDto list = createTestList();
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
