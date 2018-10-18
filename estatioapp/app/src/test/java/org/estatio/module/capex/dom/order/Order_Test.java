package org.estatio.module.capex.dom.order;

import java.math.BigDecimal;
import java.util.Set;

import com.google.common.base.Splitter;
import com.google.common.collect.Sets;

import org.jmock.Expectations;
import org.jmock.auto.Mock;
import org.joda.time.LocalDate;
import org.junit.Rule;
import org.junit.Test;

import org.apache.isis.core.unittestsupport.jmocking.JUnitRuleMockery2;

import org.estatio.module.asset.dom.Property;
import org.estatio.module.budget.dom.budgetitem.BudgetItem;
import org.estatio.module.capex.dom.invoice.IncomingInvoiceType;
import org.estatio.module.capex.dom.project.Project;
import org.estatio.module.capex.dom.util.PeriodUtil;
import org.estatio.module.charge.dom.Charge;
import org.estatio.module.party.dom.Organisation;
import org.estatio.module.tax.dom.Tax;

import static org.assertj.core.api.Assertions.assertThat;

public class Order_Test {

    @Test
    public void minimalRequiredDataToComplete() throws Exception {

        // given
        Order order = new Order();
        OrderItem item1 = new OrderItem();
        order.getItems().add(item1);

        // when
        String result = order.reasonIncomplete();

        // then
        assertThat(result).isEqualTo("type, order number, buyer, seller, (on item) description, charge, start date, end date, net amount, gross amount required");

        // and when
        order.setOrderNumber("123");
        order.setType(IncomingInvoiceType.CAPEX);
        order.setProperty(new Property());
        item1.setNetAmount(new BigDecimal("100"));
        result = order.reasonIncomplete();

        // then
        assertThat(result).isEqualTo("buyer, seller, (on item) description, charge, start date, end date, gross amount required");

        // and when
        order.setBuyer(new Organisation());
        order.setSeller(new Organisation());
        item1.setDescription("blah");
        item1.setGrossAmount(BigDecimal.ZERO);
        item1.setCharge(new Charge());
        item1.setStartDate(new LocalDate());
        item1.setEndDate(new LocalDate());
        result = order.reasonIncomplete();

        // then
        assertThat(result).isNull();

    }

    @Test
    public void minimalRequiredDataToComplete_consitent_dimensions_for_property_project() throws Exception {

        // given
        Order order = new Order();
        OrderItem item1 = new OrderItem();
        order.getItems().add(item1);
        order.setOrderNumber("123");
        order.setType(IncomingInvoiceType.CAPEX);
        order.setBuyer(new Organisation());
        order.setSeller(new Organisation());
        item1.setNetAmount(new BigDecimal("100"));
        item1.setDescription("blah");
        item1.setGrossAmount(BigDecimal.ZERO);
        item1.setCharge(new Charge());
        item1.setStartDate(new LocalDate());
        item1.setEndDate(new LocalDate());

        // when
        item1.setProject(new Project());
        String result = order.reasonIncomplete();

        // then
        assertThat(result).isEqualTo("property, (on item) when project filled in then property required");

    }

    @Test
    public void minimalRequiredDataToComplete_consitent_dimensions_for_property_budgetItem() throws Exception {

        // given
        Order order = new Order();
        OrderItem item1 = new OrderItem();
        order.getItems().add(item1);
        order.setOrderNumber("123");
        order.setType(IncomingInvoiceType.CAPEX);
        order.setBuyer(new Organisation());
        order.setSeller(new Organisation());
        item1.setNetAmount(new BigDecimal("100"));
        item1.setDescription("blah");
        item1.setGrossAmount(BigDecimal.ZERO);
        item1.setCharge(new Charge());
        item1.setStartDate(new LocalDate());
        item1.setEndDate(new LocalDate());

        // when
        item1.setBudgetItem(new BudgetItem());
        String result = order.reasonIncomplete();

        // then
        assertThat(result).isEqualTo("property, (on item) when budget item filled in then property required");

    }

    @Test
    public void minimalRequiredDataToComplete_consitent_dimensions_for_project_budgetItem() throws Exception {

        // given
        Order order = new Order();
        OrderItem item1 = new OrderItem();
        order.getItems().add(item1);
        order.setOrderNumber("123");
        order.setType(IncomingInvoiceType.CAPEX);
        order.setProperty(new Property());
        order.setBuyer(new Organisation());
        order.setSeller(new Organisation());
        item1.setNetAmount(new BigDecimal("100"));
        item1.setDescription("blah");
        item1.setGrossAmount(BigDecimal.ZERO);
        item1.setCharge(new Charge());
        item1.setStartDate(new LocalDate());
        item1.setEndDate(new LocalDate());
        item1.setProperty(new Property());

        // when
        item1.setBudgetItem(new BudgetItem());
        item1.setProject(new Project());
        String result = order.reasonIncomplete();

        // then
        assertThat(result).isEqualTo("(on item) either project or budget item - not both required");

    }

    @Rule
    public JUnitRuleMockery2 context = JUnitRuleMockery2.createFor(JUnitRuleMockery2.Mode.INTERFACES_AND_CLASSES);

    @Mock
    OrderItemRepository mockOrderItemRepository;

    @Test
    public void splitItem_mixin_works() throws Exception {

        // given
        Order order = new Order();
        order.orderItemRepository = mockOrderItemRepository;

        String description = "some description";
        Tax tax = new Tax();
        Charge charge = new Charge();
        Property property = new Property();
        Project project = new Project();
        BudgetItem budgetItem = new BudgetItem();
        String period = "F2018";

        OrderItem itemToSplit = new OrderItem();

        BigDecimal newItemNetAmount = new BigDecimal("50.00");
        BigDecimal newItemVatAmount = new BigDecimal("10");
        BigDecimal newItemGrossAmount = new BigDecimal("60.00");

        // expect
        context.checking(new Expectations() {{
            oneOf(mockOrderItemRepository).upsert(
                    order,
                    charge,
                    description,
                    newItemNetAmount,
                    newItemVatAmount,
                    newItemGrossAmount,
                    tax,
                    PeriodUtil.yearFromPeriod(period).startDate(),
                    PeriodUtil.yearFromPeriod(period).endDate(),
                    property,
                    project,
                    budgetItem);
        }});

        // when
        order.splitItem(itemToSplit, description, newItemNetAmount, newItemVatAmount, tax, newItemGrossAmount, charge, property, project, budgetItem, period);

    }

    @Test
    public void mergeItem_mixin_works() throws Exception {

        // given
        Order order = new Order();
        order.orderItemRepository = mockOrderItemRepository;

        OrderItem sourceItem = new OrderItem();
        OrderItem targetItem = new OrderItem();

        // expect
        context.checking(new Expectations() {{
            oneOf(mockOrderItemRepository).mergeItems(
                    sourceItem, targetItem);
        }});

        // when
        order.mergeItems(sourceItem, targetItem);

    }

    @Test
    public void mergingComments() throws Exception {
        Set<String> s1 = Sets.newLinkedHashSet(Splitter.on(" ").split("Scansione documentazione pratica VV.FF."));
        Set<String> s2 = Sets.newLinkedHashSet(Splitter.on(" ").split("Scansione documentazione pratica VV.FF. - Integrazione n. 1"));
        Set<String> s3 = Sets.newLinkedHashSet(Splitter.on(" ").split("Scansione documentazione pratica VV.FF. - Integrazione n. 2"));
        Set<String> s4 = Sets.newLinkedHashSet(Splitter.on(" ").split("Scansione documentazione pratica VV.FF. - Integrazione n. 3"));
        Set<String> s5 = Sets.newLinkedHashSet(Splitter.on(" ").split("Scansione documentazione pratica VV.FF. - Integrazione n. 4"));
        Set<String> s6 = Sets.newLinkedHashSet(Splitter.on(" ").split("Scansione documentazione pratica VV.FF. - Integrazione n. 5"));
        Set<String> s7 = Sets.newLinkedHashSet(Splitter.on(" ").split("Scansione documentazione pratica VV.FF. - Integrazione n. 6"));

        Set<String> intersection1 = Sets.union(s1, s2);
        Set<String> intersection2 = Sets.union(intersection1, s3);
        Set<String> intersection3 = Sets.union(intersection2, s4);
        Set<String> intersection4 = Sets.union(intersection3, s5);
        Set<String> intersection5 = Sets.union(intersection4, s6);
        Set<String> intersection6 = Sets.union(intersection5, s7);

        final String result = String.join(" ", intersection6);

        assertThat(result).isEqualTo("Scansione documentazione pratica VV.FF. - Integrazione n. 1 2 3 4 5 6");

    }

}