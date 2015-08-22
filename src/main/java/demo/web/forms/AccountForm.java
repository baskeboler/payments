package demo.web.forms;

import com.vaadin.server.FontAwesome;
import com.vaadin.ui.Component;
import com.vaadin.ui.Label;
import com.vaadin.ui.TextField;
import demo.jpa.entities.Account;
import demo.jpa.entities.Bank;
import demo.jpa.entities.City;
import demo.jpa.entities.Country;
import demo.jpa.repositories.BankRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.vaadin.viritin.BeanBinder;
import org.vaadin.viritin.LazyList;
import org.vaadin.viritin.MBeanFieldGroup;
import org.vaadin.viritin.fields.MTextField;
import org.vaadin.viritin.fields.TypedSelect;
import org.vaadin.viritin.form.AbstractForm;
import org.vaadin.viritin.label.RichText;
import org.vaadin.viritin.layouts.MFormLayout;
import org.vaadin.viritin.layouts.MHorizontalLayout;
import org.vaadin.viritin.layouts.MVerticalLayout;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collector;
import java.util.stream.Collectors;

/**
 * Created by victor on 8/9/15.
 */
public class AccountForm extends AbstractForm<Account> {

    private static final int PAGE_SIZE = 25;
    private final Collection<Country> countries;
    private final Collection<City> cities;

    TextField name = new MTextField("Name");

    TextField address = new MTextField("Address");
    MTextField accountBalance = new MTextField("Balance")
            .withNullRepresentation("-- Nothing --")
            .withReadOnly(true);
    TypedSelect<Bank> bank = new TypedSelect<>("Bank");
    TypedSelect<Country> country = new TypedSelect<>("Country");
    TypedSelect<City> city = new TypedSelect<>("City");

    @Override
    protected Component createContent() {
        return new MVerticalLayout(
                new MFormLayout(
                        bank,
                        name,
                        address,
                        accountBalance
                ).withWidth(""),
                new MHorizontalLayout(country, city),
                getToolbar()
        ).withWidth("");
    }

    public AccountForm(Account account, Collection<Bank> banks,
                       Collection<Country> countries, Collection<City> cities) {
        setEagerValidation(true);
        this.countries = countries;
        this.cities = cities;
        bank.setBeans(
                banks
        );
        bank.setCaptionGenerator(option -> option.getName());
        country.setCaptionGenerator(option -> option.getName());
        city.setCaptionGenerator(option -> option.getName());
        country.setBeans(countries);
        country.addMValueChangeListener(event -> {
            Country c = event.getValue();
            if (c != null) {
                List<City> cityList = cities.stream()
                        .filter(c1 -> c1.getCountry().getId() == c.getId())
                        .collect(Collectors.toList());
                city.setBeans(cityList);
                city.setEnabled(true);
            } else {
                city.setEnabled(false);
            }
        });
        setSizeUndefined();
        setEntity(account);
        accountBalance.setReadOnly(true);
        accountBalance.setEnabled(false);
    }

    @Override
    public MBeanFieldGroup<Account> setEntity(Account entity) {
        if (entity != null && entity.getCity() != null)
            country.setValue(entity.getCity().getCountry());
        MBeanFieldGroup<Account> ret = super.setEntity(entity);
        accountBalance.setReadOnly(true);
        return ret;
    }
}
