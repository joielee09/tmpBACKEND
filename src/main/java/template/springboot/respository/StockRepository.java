package template.springboot.respository;

import org.springframework.stereotype.Repository;
import template.springboot.domain.Item;

import java.util.List;

@Repository
public interface StockRepository {
    List<Item> getStock();
    List<Item> getItem(Integer ID);
}
