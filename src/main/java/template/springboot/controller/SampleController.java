package template.springboot.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import template.springboot.domain.Item;
import template.springboot.service.StockService;

import java.util.List;

@Controller
public class SampleController {

    private final StockService stockService;

    @Autowired
    public SampleController(StockService stockService) {
        this.stockService = stockService;
    }

    @GetMapping("home")
    public String helloSample(@RequestParam(value = "data", required = false) String data, Model model){
        model.addAttribute("data", data);
        return "index";
    }

    @GetMapping("api/stock")
    @CrossOrigin("*")
    @ResponseBody
    public List<Item> getStock(){ return stockService.getStock(); }

    @GetMapping("api/item")
    @CrossOrigin("*")
    @ResponseBody
    public List<Item> getItem(@RequestParam(value = "id", required = true) Integer id){
        return stockService.getItem(id);
    }
}
