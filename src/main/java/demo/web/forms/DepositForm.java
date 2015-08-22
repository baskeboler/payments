package demo.web.forms;

import com.vaadin.ui.Component;
import com.vaadin.ui.TextArea;
import com.vaadin.ui.TextField;
import demo.jpa.entities.DepositTransaction;
import org.vaadin.viritin.fields.MTextArea;
import org.vaadin.viritin.fields.MTextField;
import org.vaadin.viritin.form.AbstractForm;
import org.vaadin.viritin.layouts.MFormLayout;
import org.vaadin.viritin.layouts.MVerticalLayout;

/**
 * Created by victor on 8/9/15.
 */
public class DepositForm extends AbstractForm<DepositTransaction> {
    final TextField amount = new MTextField("Amount");
    final TextArea comments = new MTextArea("Comments");

    public DepositForm(DepositTransaction depo) {
        this.setSizeUndefined();
        this.setEntity(depo);
    }

    @Override
    protected Component createContent() {
        return new MVerticalLayout(
                new MFormLayout(
                        this.amount,
                        this.comments
                ).withWidth(""),
                this.getToolbar()
        ).withWidth("");
    }
}
