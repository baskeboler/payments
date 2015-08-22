package demo.web.forms;

import com.vaadin.ui.Component;
import com.vaadin.ui.TextField;
import demo.jpa.entities.Account;
import demo.jpa.entities.Bank;
import demo.jpa.entities.City;
import demo.jpa.entities.Country;
import org.vaadin.viritin.MBeanFieldGroup;
import org.vaadin.viritin.fields.MTextField;
import org.vaadin.viritin.fields.TypedSelect;
import org.vaadin.viritin.form.AbstractForm;
import org.vaadin.viritin.layouts.MFormLayout;
import org.vaadin.viritin.layouts.MHorizontalLayout;
import org.vaadin.viritin.layouts.MVerticalLayout;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by victor on 8/9/15.
 */
public class AccountForm extends AbstractForm<Account> {

    private static final int PAGE_SIZE = 25;

    final TextField name = new MTextField("Name");

    final TextField address = new MTextField("Address");
    final MTextField accountBalance = new MTextField("Balance")
            .withNullRepresentation("-- Nothing --")
            .withReadOnly(true);
    final TypedSelect<Bank> bank = new TypedSelect<>("Bank");
    final TypedSelect<Country> country = new TypedSelect<>("Country");
    final TypedSelect<City> city = new TypedSelect<>("City");

    public AccountForm(Account account, Collection<Bank> banks,
                       Collection<Country> countries, Collection<City> cities) {
        this.setEagerValidation(true);
        this.bank.setBeans(
                banks
        );
        this.bank.setCaptionGenerator(Bank::getName);
        this.country.setCaptionGenerator(Country::getName);
        this.city.setCaptionGenerator(City::getName);
        this.country.setBeans(countries);
        this.country.addMValueChangeListener(event -> {
            Country c = event.getValue();
            if (c != null) {
                List<City> cityList = cities.stream()
                        .filter(c1 -> c1.getCountry().getId() == c.getId())
                        .collect(Collectors.toList());
                this.city.setBeans(cityList);
                this.city.setEnabled(true);
            } else {
                this.city.setEnabled(false);
            }
        });
        this.setSizeUndefined();
        this.setEntity(account);
        this.accountBalance.setReadOnly(true);
        this.accountBalance.setEnabled(false);
    }

    @SuppressWarnings("unchecked")
    @Override
    public MBeanFieldGroup<Account> setEntity(Account entity) {
        if (entity != null && entity.getCity() != null)
            this.country.setValue(entity.getCity().getCountry());
        MBeanFieldGroup<Account> ret = super.setEntity(entity);
        this.accountBalance.setReadOnly(true);
        return ret;
    }

    @Override
    protected Component createContent() {
        return new MVerticalLayout(
                new MFormLayout(
                        this.bank,
                        this.name,
                        this.address,
                        this.accountBalance
                ).withWidth(""),
                new MHorizontalLayout(this.country, this.city),
                this.getToolbar()
        ).withWidth("");
    }
}
