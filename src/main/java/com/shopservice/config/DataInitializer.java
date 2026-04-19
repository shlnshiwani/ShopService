package com.shopservice.config;

import com.shopservice.entity.*;
import com.shopservice.repository.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class DataInitializer implements CommandLineRunner {

    private final UserRepository userRepo;
    private final ProductRepository productRepo;
    private final FaqRepository faqRepo;

    @Value("${app.seed-data:true}")
    private boolean seedData;

    @Override
    public void run(String... args) {
        if (!seedData) {
            log.info("Data seeding disabled (app.seed-data=false)");
            return;
        }
        seedUsers();
        seedProducts();
        seedFaqs();
    }

    private void seedUsers() {
        if (userRepo.count() == 0) {
            userRepo.save(User.builder().username("admin").password("password").build());
            log.info("Seeded default admin user");
        }
    }

    private void seedProducts() {
        if (productRepo.count() > 0) return;

        List<Product> products = List.of(
            Product.builder().id("p1").name("Wireless Headphones").price(49.99).category("audio")
                .img("https://placehold.co/200x140?text=Headphones")
                .description("Premium wireless headphones with 30-hour battery life and noise isolation.")
                .specs(List.of("Driver: 40mm","Frequency: 20Hz-20kHz","Battery: 30h","Connectivity: Bluetooth 5.0")).build(),

            Product.builder().id("p2").name("Mechanical Keyboard").price(89.99).category("accessories")
                .img("https://placehold.co/200x140?text=Keyboard")
                .description("Tactile mechanical keyboard with RGB backlight and full N-key rollover.")
                .specs(List.of("Switches: Blue","Layout: TKL","Backlight: RGB","Interface: USB-C")).build(),

            Product.builder().id("p3").name("USB-C Hub").price(29.99).category("accessories")
                .img("https://placehold.co/200x140?text=USB+Hub")
                .description("7-in-1 USB-C hub with HDMI, USB-A, SD card reader and 100W PD charging.")
                .specs(List.of("Ports: 7","HDMI: 4K@30Hz","USB-A: 3x USB 3.0","PD: 100W")).build(),

            Product.builder().id("p4").name("Webcam HD").price(59.99).category("accessories")
                .img("https://placehold.co/200x140?text=Webcam")
                .description("1080p HD webcam with autofocus, stereo mic and wide-angle lens.")
                .specs(List.of("Resolution: 1080p","FPS: 30","FOV: 90°","Mic: Stereo")).build(),

            Product.builder().id("p5").name("Smart Watch").price(199.99).category("wearables")
                .img("https://placehold.co/200x140?text=SmartWatch")
                .description("Feature-packed smart watch with heart rate monitor, GPS and 7-day battery.")
                .specs(List.of("Display: AMOLED","Battery: 7 days","Water: 5ATM","GPS: Built-in")).build(),

            Product.builder().id("p6").name("Laptop Stand").price(39.99).category("accessories")
                .img("https://placehold.co/200x140?text=LaptopStand")
                .description("Adjustable aluminium laptop stand with ventilation slots and non-slip pads.")
                .specs(List.of("Material: Aluminium","Angle: 15°-45°","Fits: 10\"-17\"","Weight: 0.8kg")).build(),

            Product.builder().id("p7").name("Noise Cancelling Earbuds").price(79.99).category("audio")
                .img("https://placehold.co/200x140?text=Earbuds")
                .description("True wireless earbuds with active noise cancellation and 24h total playtime.")
                .specs(List.of("ANC: Active","Battery: 8h (bud)","Case: 16h extra","IPX: IPX5")).build(),

            Product.builder().id("p8").name("Gaming Mouse").price(45.99).category("accessories")
                .img("https://placehold.co/200x140?text=Mouse")
                .description("Precision gaming mouse with 16000 DPI sensor and 7 programmable buttons.")
                .specs(List.of("DPI: 16000","Buttons: 7","Polling: 1000Hz","Weight: 95g")).build(),

            Product.builder().id("p9").name("External SSD").price(89.99).category("storage")
                .img("https://placehold.co/200x140?text=SSD")
                .description("Portable 1TB SSD with USB 3.2 Gen2 delivering up to 1050MB/s read speed.")
                .specs(List.of("Capacity: 1TB","Read: 1050MB/s","Write: 1000MB/s","Interface: USB-C")).build(),

            Product.builder().id("p10").name("LED Desk Lamp").price(34.99).category("accessories")
                .img("https://placehold.co/200x140?text=DeskLamp")
                .description("Touch-controlled LED desk lamp with 5 colour temperatures and USB charging port.")
                .specs(List.of("Brightness: 5 levels","Colour temp: 2700K-6500K","USB charging: Yes","Power: 12W")).build(),

            Product.builder().id("p11").name("Portable Charger").price(29.99).category("accessories")
                .img("https://placehold.co/200x140?text=Charger")
                .description("20000mAh power bank with dual USB-A and USB-C output, supports 18W fast charge.")
                .specs(List.of("Capacity: 20000mAh","Output: 18W","Ports: 3","Weight: 340g")).build(),

            Product.builder().id("p12").name("Bluetooth Speaker").price(54.99).category("audio")
                .img("https://placehold.co/200x140?text=Speaker")
                .description("Waterproof portable speaker with 360° sound, 12h playtime and built-in mic.")
                .specs(List.of("Power: 20W","Battery: 12h","Water: IPX7","Connectivity: Bluetooth 5.0")).build()
        );

        productRepo.saveAll(products);
        log.info("Seeded {} products", products.size());
    }

    private void seedFaqs() {
        if (faqRepo.count() > 0) return;

        List<Faq> faqs = List.of(
            Faq.builder().id(1).question("What is your return policy?")
                .answer("We accept returns within 30 days of delivery. Items must be unused and in original packaging. Contact support to initiate a return.").build(),
            Faq.builder().id(2).question("How long does shipping take?")
                .answer("Standard shipping takes 3-5 business days. Express shipping (1-2 days) is available at checkout for an additional fee.").build(),
            Faq.builder().id(3).question("Do you offer international shipping?")
                .answer("Yes, we ship to over 50 countries. International delivery typically takes 7-14 business days depending on the destination.").build(),
            Faq.builder().id(4).question("How can I track my order?")
                .answer("Once your order is dispatched you will receive an email with a tracking number. Use your Order ID in the Orders page to view status.").build(),
            Faq.builder().id(5).question("What payment methods do you accept?")
                .answer("We accept Visa, MasterCard, and American Express. All transactions are processed securely via our payment gateway.").build(),
            Faq.builder().id(6).question("Is my payment information secure?")
                .answer("Yes. All payment data is encrypted using TLS 1.3 and we never store raw card numbers on our servers.").build(),
            Faq.builder().id(7).question("Can I change or cancel my order?")
                .answer("Orders can be modified or cancelled within 2 hours of placement. After that, the order enters fulfilment and cannot be changed.").build(),
            Faq.builder().id(8).question("How do I contact customer support?")
                .answer("Email us at support@shopdemo.com or use the live chat on our website. Support hours are Mon-Fri 9am-6pm.").build(),
            Faq.builder().id(9).question("Do you offer gift wrapping?")
                .answer("Yes! Gift wrapping is available for an additional $5. Add a personalised message at checkout.").build(),
            Faq.builder().id(10).question("Are products covered by a warranty?")
                .answer("All products carry a 1-year manufacturer warranty against defects. Extended warranties of up to 3 years are available for select items.").build()
        );

        faqRepo.saveAll(faqs);
        log.info("Seeded {} FAQs", faqs.size());
    }
}
