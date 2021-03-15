//package jsf.managedbean;
//
//import entity.ProductEntity;
//import java.io.Serializable;
//import javax.annotation.PostConstruct;
//import javax.inject.Named;
//import javax.faces.view.ViewScoped;
//
//
//
//@Named(value = "viewProductManagedBean")
//@ViewScoped
//
//public class ViewProductManagedBean implements Serializable
//{
//    private ProductEntity productEntityToView;
//    
//    
//    
//    public ViewProductManagedBean()
//    {
//        productEntityToView = new ProductEntity();
//    }
//    
//    
//    
//    @PostConstruct
//    public void postConstruct()
//    {        
//    }
//
//    
//    
//    public ProductEntity getProductEntityToView() {
//        return productEntityToView;
//    }
//
//    public void setProductEntityToView(ProductEntity productEntityToView) {
//        this.productEntityToView = productEntityToView;
//    }    
//}
