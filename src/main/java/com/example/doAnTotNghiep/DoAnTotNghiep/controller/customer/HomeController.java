package com.example.doAnTotNghiep.DoAnTotNghiep.controller.customer;

import com.example.doAnTotNghiep.DoAnTotNghiep.config.CustomUserDetails;
import com.example.doAnTotNghiep.DoAnTotNghiep.model.*;
import com.example.doAnTotNghiep.DoAnTotNghiep.service.*;
import com.google.gson.Gson;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/")
public class HomeController {
    private final ProductService productService;
    private final ProductDetailService productDetailService;
    private final AuthorService authorService;
    private final PublisherService publisherService;
    private final CategoryService categoryService;
    private final CategoryProductService categoryProductService;
    private final ReviewService reviewService;
    private final YoutubeService youtubeService;
    private final VideoService videoService;

    public HomeController(ProductService productService, ProductDetailService productDetailService, AuthorService authorService, PublisherService publisherService, CategoryService categoryService, CategoryProductService categoryProductService, ReviewService reviewService, YoutubeService youtubeService, VideoService videoService) {
        this.productService = productService;
        this.productDetailService = productDetailService;
        this.authorService = authorService;
        this.publisherService = publisherService;
        this.categoryService = categoryService;
        this.categoryProductService = categoryProductService;
        this.reviewService = reviewService;
        this.youtubeService = youtubeService;
        this.videoService = videoService;
    }
    @GetMapping("/")
    public String home(Model model, @RequestParam(defaultValue = "0") int page){
        Product p = new Product();
        System.out.println(p.toString());
        List<Author> authors = authorService.getAll();
        model.addAttribute("authors", authors);
        List<Category> categories = categoryService.getAll();
        model.addAttribute("categories", categories);
        int pageSize = 8;
        Page<ProductDetail> tPage = productDetailService.getByPage(page, pageSize, new ProductDetail());
        model.addAttribute("tPage", tPage);
        return "/customer/index";
    }
    @GetMapping("/category-product/{id}")
    public String getProductDetailByAuthor(@PathVariable("id")Long id, Model model, @RequestParam(defaultValue = "0") int page) {
        List<Category> categories = categoryService.getAll();
        model.addAttribute("categories", categories);
        List<CategoryProduct> categoryProductList = categoryProductService.findByCategory(id);
        List<ProductDetail> results = new ArrayList<>();
        for (CategoryProduct c : categoryProductList) {
            List<ProductDetail> list = productDetailService.findByProduct(c.getProduct().getId());
            for (ProductDetail p : list) {
                results.add(p);
            }
        }
        int pageSize = 8;
        int start = page * pageSize;
        int end = Math.min(start + pageSize, results.size());
        Page<ProductDetail> tPage = new PageImpl<>(results.subList(start, end), PageRequest.of(page, pageSize), results.size());
        model.addAttribute("tPage", tPage);
        return "/customer/index";
    }
    @GetMapping("/product-detail/{id}")
    public String productDetail(@PathVariable("id")Long id, @RequestParam(defaultValue = "0") int page, Model model) throws IOException, InterruptedException {
        List<Category> categories = categoryService.getAll();
        model.addAttribute("categories", categories);
        ProductDetail productDetail = productDetailService.getById(id);
        model.addAttribute("product", productDetail);
        Review review = new Review();
        model.addAttribute("reviewObj", review);
        Review review1 = new Review();
        review1.setProductDetail(productDetail);
        Page<Review> reviews = reviewService.getPageByProductDetail(page, 5, review1);
        model.addAttribute("tPage", reviews);
        Product product = productDetail.getProduct();
        Gson gson = new Gson();
        String[] preImgs = gson.fromJson(product.getPreImg(), String[].class);
        model.addAttribute("preImgs", preImgs);
        List<Video> videos = videoService.findByProductDetail(id);
        if(videos.isEmpty()){
            if(product.getAuthor()!=null){
                getDataYoutube("Review sách "+product.getName()+" "+product.getAuthor().getName(), productDetail);
            } else{
                getDataYoutube("Review sách "+product.getName(), productDetail);
            }
            videos=videoService.findByProductDetail(id);
        }
        model.addAttribute("videos", videos);
        return "customer/product-detail";
    }
    @PostMapping("/search/product")
    public String searchProduct(@RequestParam("searchProduct")String search,
                                @RequestParam(defaultValue = "0") int page,
                                Model model){
        List<Category> categories = categoryService.getAll();
        model.addAttribute("categories", categories);
        List<ProductDetail> productDetails = productDetailService.getAll();
        List<ProductDetail> tPage = new ArrayList<>();
        for (ProductDetail p: productDetails){
            if(p.getProduct().getName().contains(search)) tPage.add(p);
        }
        model.addAttribute("tPage", tPage);
        return "/customer/list-product";
    }
    @PostMapping("/review/create/{id}")
    public String createReview(@ModelAttribute("reviewObj") Review review, @PathVariable("id") Long id){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        Account account = userDetails.getUser();
        review.setAccount(account);
        ProductDetail productDetail = productDetailService.getById(id);
        review.setProductDetail(productDetail);
        Review review1 = new Review();
        review1.setReview(review.getReview());
        review1.setRating(review.getRating());
        review1.setAccount(review.getAccount());
        review1.setProductDetail(review.getProductDetail());
        reviewService.create(review1);
        return "redirect:/product-detail/"+productDetail.getId();
    }
    @GetMapping("/video/{videoId}")
    public String videoUnit(@PathVariable("videoId")String videoId, Model model){
        List<Category> categories = categoryService.getAll();
        model.addAttribute("categories", categories);
        model.addAttribute("videoId", videoId);
        return "/customer/review_youtube";
    }
    @PostMapping("/video/search")
    public String videoSearch(Model model, @RequestParam("searchQuery") String searchQuery){
        try{
            List<Video> videos = youtubeService.fetchDatan(searchQuery);
            model.addAttribute("videos",videos);
        } catch (Exception e){
            e.printStackTrace();
            return "redirect:/error/404";
        }
        List<Category> categories = categoryService.getAll();
        model.addAttribute("categories", categories);
        return "/customer/list_review_youtube";
    }
    private void getDataYoutube(String str, ProductDetail productDetail) throws IOException, InterruptedException {
        List<Video> videos = youtubeService.fetchData(str);
        for(Video video:videos) {
            video.setProductDetail(productDetail);
            videoService.create(video);
        }
    }

}
