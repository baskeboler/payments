package demo.web.forms;

import com.vaadin.ui.Component;
import com.vaadin.ui.TextArea;
import com.vaadin.ui.TextField;
import demo.jpa.entities.Account;
import demo.jpa.entities.DepositTransaction;
import demo.jpa.repositories.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.vaadin.viritin.LazyList;
import org.vaadin.viritin.fields.MTextArea;
import org.vaadin.viritin.fields.MTextField;
import org.vaadin.viritin.fields.TypedSelect;
import org.vaadin.viritin.form.AbstractForm;
import org.vaadin.viritin.layouts.MFormLayout;
import org.vaadin.viritin.layouts.MVerticalLayout;

/**
 * Created by victor on 8/9/15.
 */
public class DepositForm extends AbstractForm<DepositTransaction> {
    TextField amount = new MTextField("Amount");
    TextArea comments = new MTextArea("Comments");

    public DepositForm(DepositTransaction depo) {
        setSizeUndefined();
        setEntity(depo);
    }

    @Override
    protected Component createContent() {
        return new MVerticalLayout(
                new MFormLayout(
                        amount,
                        comments
                ).withWidth(""),
                getToolbar()
        ).withWidth("");
    }
}
