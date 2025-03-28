package com.example.travel_master_yyz;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.ViewFlipper;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

public class AgreetmentActivity extends AppCompatActivity {

    TextView title,content;
    Toolbar toolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agreetment);
        content = findViewById(R.id.tv_content_1);
        toolbar = findViewById(R.id.toolbar);
        toolbar.setNavigationOnClickListener(v -> onBackPressed());
        title = findViewById(R.id.tv_title);
        boolean isAgreement = getIntent().getBooleanExtra("isAgreement", false);
        if (isAgreement) {
            title.setText("用户协议");
            content.setText("用户协议\n" +
                    "生效日期：2025年1月1日\n" +
                    "\n" +
                    "欢迎使用我们的应用程序（以下简称“本应用”）。在您使用本应用之前，请仔细阅读本用户协议（以下简称“协议”）。通过使用本应用，您同意遵守本协议的所有条款和条件。如果您不同意本协议的任何条款，请立即停止使用本应用。\n" +
                    "\n" +
                    "1. 使用许可\n" +
                    "1.1 我们授予您有限的、非独占的、不可转让的许可，以使用本应用。您不得将本应用用于任何商业目的，除非获得我们的明确书面许可。\n" +
                    "\n" +
                    "1.2 您不得复制、修改、分发、出售或出租本应用的任何部分，除非法律允许或我们书面同意。\n" +
                    "\n" +
                    "2. 用户责任\n" +
                    "2.1 您同意在使用本应用时遵守所有适用的法律和法规。\n" +
                    "\n" +
                    "2.2 您不得使用本应用进行任何非法活动或侵犯他人权利的行为。\n" +
                    "\n" +
                    "2.3 您应对您在本应用上的所有行为负责，包括但不限于发布的内容和与他人的互动。\n" +
                    "\n" +
                    "3. 知识产权\n" +
                    "3.1 本应用及其所有内容（包括但不限于文本、图形、 logos、图标、图像、音频剪辑、数字下载和软件）均为我们的财产或我们的许可方的财产，受版权和其他知识产权法律的保护。\n" +
                    "\n" +
                    "3.2 未经我们明确书面许可，您不得使用本应用的任何内容。\n" +
                    "\n" +
                    "4. 终止\n" +
                    "4.1 我们保留随时终止或暂停您访问本应用的权利，无需事先通知或承担任何责任。\n" +
                    "\n" +
                    "4.2 如果您违反本协议的任何条款，您的使用许可将自动终止，您必须立即停止使用本应用。\n" +
                    "\n" +
                    "5. 免责声明\n" +
                    "5.1 本应用按“原样”提供，我们不提供任何形式的明示或暗示的保证，包括但不限于适销性、特定用途适用性和非侵权的保证。\n" +
                    "\n" +
                    "5.2 我们不保证本应用将始终可用、无错误或安全。\n" +
                    "\n" +
                    "6. 法律适用\n" +
                    "6.1 本协议受中华人民共和国法律管辖并按其解释。\n" +
                    "\n" +
                    "6.2 任何因本协议引起的或与本协议相关的争议，应提交至我们所在地有管辖权的人民法院解决。");
        } else {
            title.setText("隐私政策");
            content.setText("隐私政策\n" +
                    "生效日期：2025年1月1日\n" +
                    "\n" +
                    "我们尊重并保护您的隐私。本隐私政策解释了我们在您使用本应用时如何收集、使用、存储和保护您的个人信息。\n" +
                    "\n" +
                    "1. 信息收集\n" +
                    "1.1 我们可能会收集以下类型的个人信息：\n" +
                    "\n" +
                    "注册信息：如您的姓名、电子邮件地址、电话号码等。\n" +
                    "\n" +
                    "使用信息：如您的设备信息、IP地址、浏览器类型、访问时间和页面浏览记录等。\n" +
                    "\n" +
                    "位置信息：如您的地理位置（如果您启用了位置服务）。\n" +
                    "\n" +
                    "2. 信息使用\n" +
                    "2.1 我们可能将您的个人信息用于以下目的：\n" +
                    "\n" +
                    "提供、维护和改进本应用。\n" +
                    "\n" +
                    "处理您的请求和交易。\n" +
                    "\n" +
                    "发送通知和更新。\n" +
                    "\n" +
                    "进行数据分析和研究。\n" +
                    "\n" +
                    "3. 信息共享\n" +
                    "3.1 我们不会将您的个人信息出售、交易或转让给第三方，除非以下情况：\n" +
                    "\n" +
                    "获得您的明确同意。\n" +
                    "\n" +
                    "为提供您所需的服务而必须与第三方共享。\n" +
                    "\n" +
                    "根据法律要求或响应合法的法律程序。\n" +
                    "\n" +
                    "4. 信息安全\n" +
                    "4.1 我们采取合理的技术和组织措施来保护您的个人信息，防止未经授权的访问、使用或披露。\n" +
                    "\n" +
                    "4.2 尽管我们尽力保护您的个人信息，但没有任何安全措施是绝对安全的，因此我们无法保证信息的绝对安全。\n" +
                    "\n" +
                    "5. 您的权利\n" +
                    "5.1 您可以随时访问、更正或删除您的个人信息。您也可以通过联系我们行使您的权利。\n" +
                    "\n" +
                    "5.2 您可以随时选择不接收我们的营销信息。\n" +
                    "\n" +
                    "6. 隐私政策的变更\n" +
                    "6.1 我们可能会不时更新本隐私政策。任何变更将在本页面上发布，并在生效日期生效。\n" +
                    "\n" +
                    "6.2 我们建议您定期查看本隐私政策以了解任何更新。\n" +
                    "\n" +
                    "7. 联系我们\n" +
                    "7.1 如果您对本隐私政策有任何疑问或意见，请通过以下方式联系我们：\n" +
                    "\n" +
                    "电子邮件：13660308758@163.com\n" +
                    "\n" +
                    "地址：广东省广州市番禺区广州大学城外环东路178号\n" +
                    "\n" +
                    "注意事项：\n" +
                    "法律合规：确保你的用户协议和隐私政策符合当地法律法规（如《中华人民共和国网络安全法》和《个人信息保护法》）。\n" +
                    "\n" +
                    "个性化调整：根据你的应用功能（如支付、社交、定位等）补充相关条款。\n" +
                    "\n" +
                    "用户同意：在用户首次使用时，通过弹窗或勾选框明确获取用户同意。");
        }
    }


}