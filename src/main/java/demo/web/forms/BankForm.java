package demo.web.forms;

import com.vaadin.ui.Component;
import com.vaadin.ui.TextField;
import demo.jpa.entities.Bank;
import org.vaadin.viritin.fields.MTextField;
import org.vaadin.viritin.form.AbstractForm;
import org.vaadin.viritin.layouts.MFormLayout;
import org.vaadin.viritin.layouts.MVerticalLayout;

/**
 * Created by victor on 8/18/15.
 */
public class BankForm extends AbstractForm<Bank> {
    private final TextField name = new MTextField("Name");
    private final TextField address = new MTextField("Address");

    public BankForm(Bank bank) {
        setSizeUndefined();
        setEntity(bank);
    }

    @Override
    protected Component createContent() {
        return new MVerticalLayout(
                new MFormLayout(
                        name,
                        address
                ).withWidth(""),
                getToolbar()
        ).withWidth("");
    }
}
