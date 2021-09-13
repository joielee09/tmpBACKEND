package template.springboot.respository;

import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import template.springboot.domain.Item;

import javax.persistence.EntityManager;
import java.util.List;

@Repository
public class JPAStockRepository implements StockRepository{

    private final EntityManager em;

    @Autowired
    public JPAStockRepository(EntityManager em) {
        this.em = em;
    }

    @Override
    public List<Item> getStock() {
        return em.createNativeQuery("SELECT * FROM STOCK", Item.class)
                .getResultList();
    }

    @Override
    public List<Item> getItem(Integer ID) {
        return em.createNativeQuery("SELECT * FROM STOCK WHERE ID="+ID, Item.class)
                .getResultList();
    }
}
