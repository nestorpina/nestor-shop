<%@page import="com.igz.entity.product.ProductDto"%>
<%@page import="com.igzcode.java.util.StringUtil"%>
<%@page import="com.igz.entity.product.ProductManager"%>
<%
String idParam = request.getParameter("id");
if(!StringUtil.isNullOrEmpty(idParam)) {
	ProductManager pm = new ProductManager();
	ProductDto product = pm.getByLongId(Long.valueOf(idParam));
	if(product==null) {
		product = new ProductDto();
	}
	pageContext.setAttribute("product", product);
}

%>

<div>
	Product : ${product.name }
</div>