package \${package}.pmo;

import org.apache.commons.lang3.StringUtils;
import org.linkki.core.ui.section.annotations.EnabledType;
import org.linkki.core.ui.section.annotations.UIButton;
import org.linkki.core.ui.section.annotations.UILabel;
import org.linkki.core.ui.section.annotations.UISection;
import org.linkki.core.ui.section.annotations.UITextField;
import org.linkki.core.ui.section.annotations.VisibleType;
import org.linkki.util.handler.Handler;

import com.vaadin.server.FontAwesome;

@UISection
public class HelloPmo {

    private String name;
    private Handler handler;

    public HelloPmo(Handler handler) {
        this.handler = handler;
    }

    @UITextField(position = 10, label = "Please type your Name and press Enter")
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @UILabel(position = 20)
    public String getHello() {
        if (name != null) {
            return "Hello " + name + "! This is a linkki Example Web Application.";
        } else {
            return "";
        }
    }

    @UIButton(position = 30, showIcon = true, icon = FontAwesome.INFO, enabled = EnabledType.DYNAMIC, visible = VisibleType.DYNAMIC)
    public void button() {
        handler.apply();
    }

    public boolean isButtonEnabled() {
        return StringUtils.isNoneBlank(name);
    }

    public boolean isButtonVisible() {
        return StringUtils.isNoneBlank(name);
    }

}