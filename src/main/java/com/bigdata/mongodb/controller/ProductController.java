package com.bigdata.mongodb.controller;

import com.bigdata.mongodb.entity.*;
import com.bigdata.mongodb.model.FrequentProducts;
import com.bigdata.mongodb.model.ProductModel;
import com.bigdata.mongodb.repository.BrandRepository;
import com.bigdata.mongodb.repository.CategoryRepository;
import com.bigdata.mongodb.repository.OrderRepository;
import com.bigdata.mongodb.repository.ProductRepository;
import com.bigdata.mongodb.utility.PrePost;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.criteria.CriteriaBuilder;
import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;
import java.io.IOException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

//@RestController
@Controller
@RequestMapping("/products")
@Transactional
public class ProductController {
    @Autowired
    private MongoTemplate mongoTemplate;
    @Autowired
    private ProductRepository repo;
    @Autowired
    private CategoryRepository cateRepo;
    @Autowired
    private BrandRepository brandRepo;
    @Autowired
    private OrderRepository orderRepo;
    @Autowired
    private FrequentProducts frequentProducts;

    @GetMapping("find-all")
    @ResponseBody
    public List<Product> findAll() {
        List<Product> products = repo.findAll();
        for(Product product : products) {
            product.setImages(null);
        }
        return products;
    }

    @GetMapping("")
    public String findProducts(HttpServletRequest request, Model model)
            throws NoSuchFieldException {
        String search = request.getParameter("search") == null ? "" : request.getParameter("search");
        Integer page = request.getParameter("page") == null ? 0 : Integer.parseInt(request.getParameter("page"));
        Integer limit = request.getParameter("limit") == null ? 10 : Integer.parseInt(request.getParameter("limit"));;
        String sortAscBy = request.getParameter("sortAscBy") == null ? "id" : request.getParameter("sortAscBy");
        String sortDescBy = request.getParameter("sortDescBy");

        List<Criteria> criteriaList = new ArrayList<>();
        if(search != null)
            criteriaList.add(new Criteria().orOperator(Arrays.asList(
                    Criteria.where("name").regex(".*" + search + ".*", "i"),
                    Criteria.where("category").in(cateRepo.findByNameContainingIgnoreCase(search)),
                    Criteria.where("brand").in(brandRepo.findByNameContainingIgnoreCase(search))
            )));

        Pattern patternSortProperty = Pattern.compile("(\\w+?),");

        Set<String> sortAscProperties = new HashSet<>();
        Matcher matcherSortProperty = patternSortProperty.matcher(sortAscBy + ",");
        while (matcherSortProperty.find()) {
            sortAscProperties.add(matcherSortProperty.group(1));
        }

        Set<String> sortDescProperties = new HashSet<>();
        matcherSortProperty = patternSortProperty.matcher(sortDescBy + ",");
        while (matcherSortProperty.find()) {
            sortDescProperties.add(matcherSortProperty.group(1));
        }

        Sort sort = Sort.by(sortAscProperties.stream().map(Sort.Order::asc).toList())
                .and(Sort.by(sortDescProperties.stream().map(Sort.Order::desc).toList()));

        Pageable pageable = PageRequest.of(page, limit, sort);

        Query query = new Query().with(pageable);

        if (!criteriaList.isEmpty())
            query.addCriteria(new Criteria().andOperator(criteriaList.toArray(new Criteria[criteriaList.size()])));
        List<Product> products = mongoTemplate.find(query, Product.class);

        List<ProductModel> productModels = products.stream().map(product -> {
//            if(product.getOptions() == null) {
//                productModel.setTotalBalanceQTY(0L);
//                productModel.setTotalSoldQTY(0L);
//            }
//            else {
//                product.getOptions().stream().forEach(
//                        option -> productModel.setTotalBalanceQTY(productModel.getTotalBalanceQTY() + option.getBalanceQTY()));
//                product.getOptions().stream().forEach(
//                        option -> productModel.setTotalSoldQTY(productModel.getTotalSoldQTY() + option.getSoldQTY()));
//            }
            return convertToModel(product);
        }).toList();
        model.addAttribute("pro", productModels);
        request.setAttribute("products", productModels);
        request.setAttribute("search", search);
        request.setAttribute("page", page);
        request.setAttribute("limit", limit);

        int count = mongoTemplate.find(query.with(PageRequest.of(0, (int)repo.count() + 1)),
                        Product.class).size();
        request.setAttribute("currentPage", page + 1);
        request.setAttribute("totalData", count);
        request.setAttribute("totalPage", count < 1 ? 1 : count / limit * limit < count ?
                count / limit + 1 : count / limit);
        request.setAttribute("start", page * limit + 1);
        request.setAttribute("end", page + limit < count ? page + limit : count);

        return "index";
    }

//    @GetMapping("/{id}")
//    public Product findProductById(@PathVariable String id) {
//        return repo.findById(id).get();
//    }

    @GetMapping("/update")
    public String updateBook(HttpServletRequest request, Model model) {
        String id  = request.getParameter("id");
        Product product = repo.findById(id).get();
        ProductModel productModel = new ProductModel();
        productModel = convertToModel(product);
        request.setAttribute("categories", cateRepo.findAll());
        request.setAttribute("brands", brandRepo.findAll());
        request.setAttribute("numberOfImages", productModel.getImages().size() - 1);
        model.addAttribute("product", productModel);
        return "update";
    }

    @PostMapping("/update")
    public String updateProduct(@ModelAttribute("product") ProductModel productModel) throws Exception {
        Product product = convertToEntity(productModel);
        product.setId(productModel.getId());
        repo.save(product);
        return "redirect:/products";
    }

    @GetMapping("/add")
    public String addProduct(HttpServletRequest request, Model model) {
        request.setAttribute("categories", cateRepo.findAll());
        request.setAttribute("brands", brandRepo.findAll());
        model.addAttribute("product", new ProductModel());
        return "add";
    }

    @PostMapping("/add")
    public String addProduct(@ModelAttribute("product") ProductModel productModel) throws Exception {
        Product product = convertToEntity(productModel);
        repo.save(product);
        return "redirect:/products";
    }

    @GetMapping("/delete/{id}")
    public String removeProduct(@PathVariable String id) {
        if (repo.findById(id).isPresent()) repo.deleteById(id);
        return "redirect:/products";
    }

    @GetMapping("/download/{productId}")
    @ResponseBody
    public ResponseEntity<Resource> download(@PathVariable String productId, @RequestParam(name = "index") int index) {
        Product product = repo.findById(productId).get();
        Image image = product.getImages().get(index);
        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(image.getType()))
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=\"" + image.getName()
                                + "\"")
                .body(new ByteArrayResource(image.getData()));
    }

    private ProductModel convertToModel(Product product) {
        ProductModel productModel = new ProductModel();
        productModel.setTotalBalanceQTY(product.getTotalBalanceQTY());
        productModel.setTotalSoldQTY(product.getTotalSoldQTY());
        productModel.setBrand(product.getBrand());
        productModel.setCategory(product.getCategory());
        productModel.setId(product.getId());
        productModel.setDescription(product.getDescription());
        productModel.setName(product.getName());
        productModel.setPrice(product.getPrice());
        if(!product.getImages().isEmpty()) {
            productModel.setImages(product.getImages());
        }
        return productModel;
    }

    private Product convertToEntity(ProductModel productModel) throws Exception {
        Product product = new Product();
        product.setName(productModel.getName());
        product.setSlug(productModel.getName().toLowerCase().replace(" ", "-"));
        product.setPrice(productModel.getPrice());
        product.setDescription(productModel.getDescription());
        product.setTotalBalanceQTY(productModel.getTotalBalanceQTY());
        product.setTotalSoldQTY(productModel.getTotalSoldQTY());
//        product.setOptions(productModel.getOptions());
        if (productModel.getCategory() != null && cateRepo.findById(productModel.getCategory().getId()).isPresent())
            product.setCategory(cateRepo.findById(productModel.getCategory().getId()).get());
        else product.setCategory(null);
        if (productModel.getBrand() != null && brandRepo.findById(productModel.getBrand().getId()).isPresent())
            product.setBrand(brandRepo.findById(productModel.getBrand().getId()).get());
        else product.setBrand(null);
        Iterator<MultipartFile> fileIterator = productModel.getMultipartFiles().iterator();
        List<Image> images = new ArrayList<>();
        while (fileIterator.hasNext()) {
            var file = fileIterator.next();
            String fileName = StringUtils.cleanPath(file.getOriginalFilename());
            try {
                if(fileName.contains("..")) {
                    throw  new Exception("Filename contains invalid path sequence "
                            + fileName);
                }
                Image image = new Image(fileName,
                        file.getContentType(),
                        file.getBytes());
                images.add(image);
            } catch (Exception e) {
                throw new Exception("Could not save File: " + fileName);
            }
        }
        product.setImages(images);
        return product;
    }

    @GetMapping("/charts")
    public String getPieChart(Model model) {
        Map<String, Long> graphData = new TreeMap<>();
        List<Category> categories = cateRepo.findAll();
        categories.stream().forEach(category ->
                {
                    long count = repo.findByCategory(category).stream()
                    .reduce(0L,(totalCount, product) ->  totalCount + product.getTotalBalanceQTY(), Long::sum);
                    graphData.put(category.getName(),count);
                });
        model.addAttribute("chartData", graphData);
        return "google-charts";
    }

    @GetMapping("/recommend-products")
    @ResponseBody
    public List<Product> recommendProducts(@RequestBody List<Product> products) throws IOException {
        products = products.stream().map(product -> repo.findById(product.getId()).get()).toList();
        List<Product> allProducts = repo.findAll();

        List<Integer> productsIndexing = new ArrayList<>();
        for(Product product: products){
            int index = allProducts.indexOf(product) + 1;
            productsIndexing.add(index);
        }
        System.out.println("recommend of: " + productsIndexing.toString());
        int support = 0;
        List<Integer> recommendIndex = new ArrayList<>();
        for(List<Integer> itemSet : frequentProducts.getResult().keySet()) {
            int supportOfItemSet = frequentProducts.getResult().get(itemSet);
            if (itemSet.containsAll(productsIndexing) && itemSet.size() > products.size()) {
                if(itemSet.size() > recommendIndex.size() && supportOfItemSet == support
                        || supportOfItemSet > support) {
                    System.out.println(itemSet.toString() + " support: " + supportOfItemSet);
                    support = supportOfItemSet;
                    recommendIndex = itemSet;
                }
            }
        }
        List<Product> recommendList = new ArrayList<>();
        for(Integer i : recommendIndex) {
            Product product = allProducts.get(i-1);
            if(!products.contains(product)) {
                product.setImages(null);
                recommendList.add(product);
            }
        }

        return recommendList;
    }

    @GetMapping("/frequent-products")
    @ResponseBody
    public FrequentProducts getFrequentProducts() {
        return frequentProducts;
    }

    @Scheduled(fixedDelay = 300000)
    public void scheduleScanDatabase() throws IOException {
        System.out.println("Start scan database...");
        List<Product> allProducts = repo.findAll();
        List<Order> allOrders = orderRepo.findAll();

        List<List<Integer>> listTransaction = new ArrayList<>();
        for(Order order : allOrders) {
            List<Product> transaction =
                    order.getOrderDetailsList().stream().map(orderDetails -> orderDetails.getProduct()).toList();
            List<Integer> transactionIndex = new ArrayList<>();
            for(Product product: transaction){
                int index = allProducts.indexOf(product) + 1;
                transactionIndex.add(index);
            }
            listTransaction.add(transactionIndex);
        }

        double minSup = 0;
        PrePost prepost = new PrePost();
        prepost.runAlgorithm(listTransaction, minSup);
        frequentProducts.setResult(prepost.listItemWithSupport);
        System.out.println("Finish scan database");
    }
}
