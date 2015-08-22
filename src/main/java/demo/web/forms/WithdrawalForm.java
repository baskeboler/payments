package demo.web.forms;

import com.vaadin.ui.Component;
import com.vaadin.ui.TextField;
import demo.jpa.entities.WithdrawalTransaction;
import org.vaadin.viritin.fields.MTextField;
import org.vaadin.viritin.form.AbstractForm;
import org.vaadin.viritin.layouts.MFormLayout;
import org.vaadin.viritin.layouts.MVerticalLayout;

/**
 * Created by victor on 8/18/15.
 */
public class WithdrawalForm extends AbstractForm<WithdrawalTransaction> {

    private TextField amount = new MTextField("Amount");

    @Override
    protected Component createContent() {
        return new MVerticalLayout(
                new MFormLayout(
                        amount
                ).withWidth(""),
                getToolbar()
        ).withWidth("");
    }

    public WithdrawalForm(WithdrawalTransaction t) {
        setSizeUndefined();
        setEntity(t);
    }
}
