package edu.kl.soa.item;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/api")
public class ItemController {
    private final ItemRepository items;

    public ItemController(ItemRepository items) {
        this.items = items;
    }

    /** Lists every item; this is the verification endpoint in the practical question. */
    @GetMapping("/all")
    public List<Item> all() {
        return items.findAll();
    }

    @GetMapping("/items/{id}")
    public Item get(@PathVariable Long id) {
        return findItem(id);
    }

    @PostMapping("/items")
    @ResponseStatus(HttpStatus.CREATED)
    public Item create(@Valid @RequestBody Item item) {
        return items.save(item);
    }

    @PutMapping("/items/{id}")
    public Item update(@PathVariable Long id, @Valid @RequestBody Item changes) {
        Item item = findItem(id);
        item.setName(changes.getName());
        item.setDescription(changes.getDescription());
        return items.save(item);
    }

    @DeleteMapping("/items/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        items.delete(findItem(id));
    }

    private Item findItem(Long id) {
        return items.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Item not found"));
    }
}
