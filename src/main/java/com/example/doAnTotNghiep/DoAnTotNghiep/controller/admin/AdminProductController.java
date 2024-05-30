package com.example.doAnTotNghiep.DoAnTotNghiep.controller.admin;

import com.example.doAnTotNghiep.DoAnTotNghiep.model.*;
import com.example.doAnTotNghiep.DoAnTotNghiep.service.*;
import com.google.gson.Gson;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/admin")
public class AdminProductController {
    private final ProductService productService;
    private final PublisherService publisherService;
    private final AuthorService authorService;
    private final CategoryService categoryService;
    private final ProductDetailService productDetailService;
    private final CategoryProductService categoryProductService;

    public AdminProductController(ProductService productService, PublisherService publisherService, AuthorService authorService, CategoryService categoryService, ProductDetailService productDetailService, CategoryProductService categoryProductService) {
        this.productService = productService;
        this.publisherService = publisherService;
        this.authorService = authorService;
        this.categoryService = categoryService;
        this.productDetailService = productDetailService;
        this.categoryProductService = categoryProductService;
    }


    //BASE METHODS
    //pagination T list
    private <T extends BaseModel, R extends BaseService> String tManagement(Model model, int page, String retn, R serv, T t){
        int pageSize = 10;
        Page<T> tPage = serv.getByPage(page, pageSize,t);
        model.addAttribute("tPage", tPage);
        return retn;
    }
    //T unit detail
    private <T extends BaseModel, R extends BaseService> String tUnitDetail(Model model, Long tId, String retn, R serv){
        T t = (T) serv.getById(tId);
        model.addAttribute("tUnit", t);
        return retn;
    }
    //create new T form
    private <T> String createTForm(Model model,T tForm, String retn){
        model.addAttribute("tForm", tForm);
        return retn;
    }
    //post method create new T
    private <T extends BaseModel, R extends BaseService> String createT(T t, R serv, String tName, BindingResult result, Model model){
        if(result.hasErrors()) {
            model.addAttribute("tForm", t);
            return "/admin/product/create-"+tName+"-form";
        }
        serv.create(t);
        return "redirect:/admin/"+tName;
    }
    //edit T form
    private <T, R extends BaseService> String editTForm(Model model, Long tId, R serv, String retn){
        T tForm = (T) serv.getById(tId);
        model.addAttribute("tForm", tForm);
        return retn;
    }
    //post method edit T
    private <T extends BaseModel, R extends BaseService> String editT(Model model, Long tId, T t, R serv, String tName, BindingResult result){
        if(result.hasErrors()){
            model.addAttribute("tForm", t);
            return "/admin/product/edit-"+tName+"-form";
        }
        t.setCreatedOn(serv.getById(tId).getCreatedOn());
        t.setCreatedBy(serv.getById(tId).getCreatedBy());
        t.setDeleted(serv.getById(tId).isDeleted());
        serv.edit(tId, t);
        model.addAttribute("tForm", t);
        return "redirect:/admin/"+tName;
    }
    //post method delete T
    private <T extends BaseModel, R extends BaseService> String deleteT(Long id, R serv, RedirectAttributes redirectAttributes, String retn){
        if(serv.delete(id)){
            redirectAttributes.addFlashAttribute("successMessage", "Đã xóa thành công đối tượng");
        } else{
            redirectAttributes.addFlashAttribute("errorMessage", "Không tìm thấy đối tượng để xóa");
        }
        return retn;
    }
    //PRODUCT
    @GetMapping("")
    public String home(){
        return "redirect:/admin/product";
    }
    @GetMapping("/product")
    public String productManagement(Model model, @RequestParam(defaultValue = "0") int page){
        return tManagement(model, page, "admin/product-management", productService, new Product());
    }
    @GetMapping("/product/{id}")
    public String productUnit(Model model, @PathVariable Long id){
        Product product = productService.getById(id);
        Gson gson = new Gson();
        String[] preImgs = gson.fromJson(product.getPreImg(), String[].class);
        model.addAttribute("preImgs", preImgs);
        return tUnitDetail(model, id, "/admin/product/product-unit", productService);
    }
    @PostMapping("/product/search")
    public String searchProduct(@RequestParam("searchProduct")Long search,
                                @RequestParam(defaultValue = "0") int page,
                                Model model){
        Product product = new Product();
        product.setId(search);
        Page<Product> tPage = productService.searchById(page,10,product);
        model.addAttribute("tPage", tPage);
        return "/admin/product-management";
    }
    @GetMapping("/product/create")
    public String createProductForm(Model model){
        Product product = new Product();
        List<Author> authors = authorService.getAll();
        model.addAttribute("authors", authors);
        List<Publisher> publishers = publisherService.getAll();
        model.addAttribute("publishers", publishers);
        return createTForm(model, product, "admin/product/create-product-form");
    }
    @PostMapping("/product/create")
    public String createProduct(@Valid @ModelAttribute("tForm") Product product,
                                BindingResult result,
                                Model model,
                                @RequestParam("imageCover") MultipartFile imageFile,
                                @RequestParam("images") List<MultipartFile> imageFiles){
        List<Author> authors = authorService.getAll();
        model.addAttribute("authors", authors);
        List<Publisher> publishers = publisherService.getAll();
        model.addAttribute("publishers", publishers);
        if (!imageFile.isEmpty()) {
            try {
                String fileName = imageFile.getOriginalFilename();
                byte[] bytes = imageFile.getBytes();
                Path path = Paths.get("src/main/resources/static/uploads/" + fileName);
                Files.write(path, bytes);
                product.setImage("uploads/" + fileName);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if (imageFiles != null && !imageFiles.isEmpty()) {
            List<String> imagePaths = new ArrayList<>();
            try {
                for (MultipartFile i : imageFiles) {
                    String fileName = i.getOriginalFilename();
                    byte[] bytes = i.getBytes();
                    Path path = Paths.get("src/main/resources/static/uploads/" + fileName);
                    Files.write(path, bytes);
                    imagePaths.add("uploads/" + fileName);
                }

                Gson gson = new Gson();
                String jsonImagePaths = gson.toJson(imagePaths);

                product.setPreImg(jsonImagePaths);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return createT(product, productService, "product",result, model);

    }
    @GetMapping("/product/edit/{id}")
    public String editProductForm(Model model, @PathVariable("id")Long id){
        List<Author> authors = authorService.getAll();
        model.addAttribute("authors", authors);
        List<Publisher> publishers = publisherService.getAll();
        model.addAttribute("publishers", publishers);
        return editTForm(model, id, productService, "/admin/product/edit-product-form");
    }
    @PostMapping("/product/edit/{id}")
    public String editProduct(@Valid @ModelAttribute Product product,
                              @PathVariable("id")Long id,
                              BindingResult result,
                              Model model,
                              @RequestParam("imageCover") MultipartFile imageFile,
                              @RequestParam("images") List<MultipartFile> imageFiles){
        if (!imageFile.isEmpty()) {
            try {
                String fileName = imageFile.getOriginalFilename();
                byte[] bytes = imageFile.getBytes();
                Path path = Paths.get("src/main/resources/static/uploads/" + fileName);
                Files.write(path, bytes);
                product.setImage("uploads/" + fileName);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if (imageFiles != null && !imageFiles.isEmpty()) {
            List<String> imagePaths = new ArrayList<>();
            try {
                for (MultipartFile i : imageFiles) {
                    String fileName = i.getOriginalFilename();
                    byte[] bytes = i.getBytes();
                    Path path = Paths.get("src/main/resources/static/uploads/" + fileName);
                    Files.write(path, bytes);
                    imagePaths.add("uploads/" + fileName);
                }

                Gson gson = new Gson();
                String jsonImagePaths = gson.toJson(imagePaths);

                product.setPreImg(jsonImagePaths);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return editT(model, id,product, productService, "product", result);
    }
    @PostMapping("/product/delete/{id}")
    public String deleteProduct(@PathVariable Long id, RedirectAttributes redirectAttributes){
        return deleteT(id, productService,redirectAttributes, "redirect:/admin/product");
    }


    //CATEGORY
    @GetMapping("/category")
    public String categoryManagement(Model model, @RequestParam(defaultValue = "0") int page){
        return tManagement(model,page,"admin/product/category-list",categoryService, new Category());
    }
    @GetMapping("/category/{id}")
    public String categoryUnit(@PathVariable("id") Long id, Model model){
        return tUnitDetail(model, id, "/admin/product/category-unit", categoryService);
    }
    @GetMapping("/category/create")
    public String createCategoryForm(Model model){
        Category category = new Category();
        return createTForm(model, category, "/admin/product/create-category-form");
    }
    @PostMapping("/category/create")
    public String createCategory(@Valid @ModelAttribute("tForm") Category category, BindingResult result, Model model){
        return createT(category,categoryService,"category", result, model);
    }
    @GetMapping("/category/edit/{id}")
    public String editCategoryForm(@PathVariable Long id, Model model){
        return editTForm(model, id, categoryService,"/admin/product/edit-category-form");
    }

    @PostMapping("/category/edit/{id}")
    public String editCategory(@PathVariable Long id, @Valid @ModelAttribute("tForm") Category category, BindingResult result, Model model){
        return editT(model, id, category, categoryService, "category", result);
    }
    @PostMapping("/category/delete/{id}")
    public String deleteCategory(@PathVariable Long id, RedirectAttributes redirectAttributes){
        return deleteT(id, categoryService, redirectAttributes, "redirect:/admin/category");
    }


    //AUTHOR
    @GetMapping("/author")
    public String authorManagement(Model model, @RequestParam(defaultValue = "0") int page){
        return tManagement(model, page, "/admin/product/author-list", authorService, new Author());
    }
    @GetMapping("/author/{id}")
    public String authorUnit(@PathVariable("id") Long id, Model model){
        return tUnitDetail(model, id, "/admin/product/author-unit", authorService);
    }
    @GetMapping("/author/create")
    public String createAuthorForm(Model model){
        Author author = new Author();
        return createTForm(model, author, "/admin/product/create-author-form");
    }

    @PostMapping("/author/create")
    public String createAuthor(@Valid @ModelAttribute("tForm") Author author, BindingResult result, Model model){
        return createT(author,authorService,"author", result, model);
    }
    @GetMapping("/author/edit/{id}")
    public String editAuthorForm(@PathVariable Long id, Model model){
        return editTForm(model, id, authorService,"/admin/product/edit-author-form");
    }

    @PostMapping("/author/edit/{id}")
    public String editAuthor(@PathVariable Long id, @Valid @ModelAttribute("tForm") Author author, BindingResult result, Model model){
        return editT(model, id, author, authorService, "author", result);
    }
    @PostMapping("/author/delete/{id}")
    public String deleteAuthor(@PathVariable Long id, RedirectAttributes redirectAttributes){
        return deleteT(id, authorService, redirectAttributes, "redirect:/admin/author");
    }



    //PUBLISHER
    @GetMapping("/publisher")
    public String publisherManagement(Model model, @RequestParam(defaultValue = "0") int page){
        return tManagement(model, page, "admin/product/publisher-list", publisherService, new Publisher());
    }
    @GetMapping("/publisher/{id}")
    public String publisherUnit(@PathVariable("id") Long id, Model model){
        return tUnitDetail(model, id, "admin/product/publisher-unit", publisherService);
    }
    @GetMapping("/publisher/create")
    public String createPublisherForm(Model model){
        Publisher publisher = new Publisher();
        return createTForm(model, publisher, "/admin/product/create-publisher-form");
    }

    @PostMapping("/publisher/create")
    public String createPublisher(@Valid @ModelAttribute("tForm") Publisher publisher, BindingResult result, Model model){
        return createT(publisher,publisherService,"publisher", result, model);
    }
    @GetMapping("/publisher/edit/{id}")
    public String editPublisherForm(@PathVariable Long id, Model model){
        return editTForm(model, id, publisherService,"/admin/product/edit-publisher-form");
    }

    @PostMapping("/publisher/edit/{id}")
    public String editPublisher(@PathVariable Long id, @Valid @ModelAttribute("tForm") Publisher publisher, BindingResult result, Model model){
        return editT(model, id, publisher, publisherService, "publisher", result);
    }
    @PostMapping("/publisher/delete/{id}")
    public String deletePublisher(@PathVariable Long id, RedirectAttributes redirectAttributes){
        return deleteT(id, publisherService, redirectAttributes, "redirect:/admin/publisher");
    }


    //PRODUCT-DETAIL
    //https://upload.wikimedia.org/wikipedia/commons/6/65/No-Image-Placeholder.svg
    @GetMapping("/product-detail")
    public String productDetailManagement(Model model, @RequestParam(defaultValue = "0") int page){
        return tManagement(model, page, "admin/product/product-detail-list", productDetailService, new ProductDetail());
    }
    @GetMapping("/product-detail/{id}")
    public String productDetailUnit(@PathVariable("id") Long id, Model model){
        Product product = productDetailService.getById(id).getProduct();
        Gson gson = new Gson();
        String[] preImgs = gson.fromJson(product.getPreImg(), String[].class);
        model.addAttribute("preImgs", preImgs);
        return tUnitDetail(model, id, "/admin/product/product-detail-unit", productDetailService);
    }
    @GetMapping("/product-detail/create")
    public String createProductDetailForm(Model model){
        ProductDetail productDetail = new ProductDetail();
        List<Product> products = productService.getAll();
        model.addAttribute("products", products);
        return createTForm(model, productDetail, "/admin/product/create-product-detail-form");
    }

    @PostMapping("/product-detail/create")
    public String createProductDetail(@Valid @ModelAttribute("tForm") ProductDetail productDetail, BindingResult result, Model model){
        return createT(productDetail,productDetailService,"product-detail", result, model);
    }
    @GetMapping("/product-detail/edit/{id}")
    public String editProductDetailForm(@PathVariable Long id, Model model){
        List<Product> products = productService.getAll();
        model.addAttribute("products", products);
        return editTForm(model, id, productDetailService,"/admin/product/edit-product-detail-form");
    }

    @PostMapping("/product-detail/edit/{id}")
    public String editProductDetail(@PathVariable Long id, @Valid @ModelAttribute("tForm") ProductDetail productDetail, BindingResult result, Model model){
        return editT(model, id, productDetail, productDetailService, "product-detail", result);
    }
    @PostMapping("/product-detail/delete/{id}")
    public String deleteProductDetail(@PathVariable Long id, RedirectAttributes redirectAttributes){
        return deleteT(id, productDetailService, redirectAttributes, "redirect:/admin/product-detail");
    }


    //CATEGORY PRODUCT
    @GetMapping("/category-product/create/{id}")
    public String createCategoryProductForm(@PathVariable("id") Long id, @RequestParam(defaultValue = "0") int page, Model model){
        int pageSize = 10;
        Page<Category> categoryPage = categoryService.getByPage(page, pageSize, new Category());
        model.addAttribute("tPage", categoryPage);
        Product product = productService.getById(id);
        model.addAttribute("product1", product);
        List<CategoryProduct> listCategoryProduct = categoryProductService.findByProduct(id);
        List<Long> listId = new ArrayList<>();
        for(CategoryProduct p: listCategoryProduct){
            listId.add(p.getCategory().getId());
        }
        model.addAttribute("product2", listId);
        return "admin/product/create-category-product-form";
    }
    @PostMapping("/category-product/create/{categoryId}/{productId}")
    public String createCategoryProduct(@PathVariable Long categoryId, @PathVariable Long productId, Model model){
        List<CategoryProduct> list = categoryProductService.findByCategory(categoryId);
        CategoryProduct categoryProduct = null;
        for(CategoryProduct c : list){
            if(c.getProduct().getId().equals(productId)){
                categoryProduct=c;
                break;
            }
        }
        if(categoryProduct!=null){
            categoryProduct.setDeleted(false);
            categoryProductService.edit(categoryProduct.getId(),categoryProduct);
            return "redirect:/admin/category-product/create/"+productId;
        } else{
            CategoryProduct categoroyProductItem = new CategoryProduct();
            categoroyProductItem.setCategory(categoryService.getById(categoryId));
            categoroyProductItem.setProduct(productService.getById(productId));
            categoryProductService.create(categoroyProductItem);
            model.addAttribute("success", "success");
            return "redirect:/admin/category-product/create/"+productId;
        }
    }
    @PostMapping("/category-product/delete/{categoryId}/{productId}")
    public String deleteCategoryProduct(@PathVariable Long categoryId, @PathVariable Long productId, RedirectAttributes redirectAttributes){
        List<CategoryProduct> list = categoryProductService.findByCategory(categoryId);
        CategoryProduct categoryProduct = new CategoryProduct();
        for(CategoryProduct c : list){
            if(c.getProduct().getId().equals(productId)){
                categoryProduct=c;
                break;
            }
        }
        return deleteT(categoryProduct.getId(),categoryProductService,redirectAttributes, "redirect:/admin/category-product/create/"+productId);
    }
}
