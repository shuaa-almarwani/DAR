package com.example.DAR.Service;

import com.example.DAR.DTO.In.ChatbotDTOIn;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ChatbotService {

    private final OpenAIService openAIService;

    public List<String> getSuggestedQuestions() {
        return List.of(
                "كيف أضيف منزل؟",
                "كيف أضيف جهاز؟",
                "كيف أضيف تذكير صيانة؟",
                "كيف أفعّل التنبيهات الذكية؟",
                "كيف أضيف فاتورة؟",
                "كيف أضيف فاتورة شراء؟",
                "كيف أغيّر طريقة التنبيه؟",
                "ما فائدة التنبيهات الذكية؟",
                "هل دار يوفر فنيين؟",
                "ما فائدة أولوية الصيانة؟"
        );
    }

    public String ask(ChatbotDTOIn dto) {

        String question = dto.getQuestion();

        String faqAnswer = getFAQAnswer(question);

        if (faqAnswer != null) {
            return faqAnswer;
        }

        return askAI(question);
    }

    private String getFAQAnswer(String question) {

        String q = question.toLowerCase();

        if (q.contains("اضيف منزل") || q.contains("أضيف منزل") || q.contains("منزل")) {
            return "يمكنك إضافة منزل من زر إضافة منزل جديد، ثم إدخال بيانات المنزل مثل العنوان، المدينة، نوع العقار، وسنة البناء.";
        }

        if (q.contains("اضيف جهاز") || q.contains("أضيف جهاز") || q.contains("جهاز") || q.contains("الأجهزة")) {
            return "يمكنك إضافة جهاز من صفحة الأجهزة داخل المنزل، ثم إدخال اسم الجهاز ونوعه وتفاصيله حتى تتمكن من ربطه بالصيانة والتذكيرات.";
        }

        if (q.contains("تذكير") || q.contains("reminder")) {
            return "يمكنك إضافة تذكير صيانة من صفحة الصيانة، ثم اختيار الصيانة المرتبطة، تاريخ التذكير، الموسم، محفز الطقس، وطريقة التنبيه مثل البريد الإلكتروني أو الواتساب أو الاتصال.";
        }

        if (q.contains("طريقة التنبيه") || q.contains("واتساب") || q.contains("اتصال") || q.contains("ايميل") || q.contains("إيميل")) {
            return "طرق التنبيه المتاحة في دار هي البريد الإلكتروني، الواتساب، والاتصال الآلي. يتم اختيار الطريقة عند إنشاء تذكير الصيانة.";
        }

        if (q.contains("طقس") || q.contains("weather") || q.contains("تنبيهات ذكية")) {
            return "تستخدم دار بيانات الطقس لتقديم نصائح وتنبيهات ذكية مرتبطة بالعناية بالمنزل، مثل فحص المكيف وقت الحرارة أو فحص التسريبات وقت الأمطار.";
        }

        if (q.contains("فاتورة") || q.contains("bill")) {
            return "يمكنك إضافة الفواتير لمتابعة مصاريف المنزل، كما يساعدك النظام على ملاحظة الارتفاع غير المعتاد في الفواتير.";
        }

        if (q.contains("اشتراك") || q.contains("subscription") || q.contains("دفع") || q.contains("payment")) {
            return "يمكنك اختيار باقة اشتراك مناسبة، ثم إتمام الدفع لتفعيل مزايا دار مثل إدارة المنازل والتنبيهات الذكية والتذكيرات.";
        }

        if (q.contains("فني") || q.contains("عامل") || q.contains("تصليح") || q.contains("ينفذ الصيانة")) {
            return "دار لا يوفر فنيين ولا ينفذ الصيانة، بل يساعدك على تنظيم الصيانة، متابعة الأجهزة، وجدولة التذكيرات.";
        }

        if (q.contains("أولوية") || q.contains("اولويه") || q.contains("priority")) {
            return "أولوية الصيانة تساعد المستخدم على تمييز أهمية الصيانة، مثل منخفضة أو متوسطة أو عالية أو عاجلة، وتظهر في التذكيرات والتنبيهات.";
        }

        return null;
    }

    private String askAI(String question) {

        String prompt = """
                You are DAR Assistant, an Arabic chatbot for a smart home care and maintenance reminder platform.

                DAR helps users:
                - manage homes
                - manage home items and devices
                - organize maintenance
                - create maintenance reminders
                - receive weather-based home care alerts
                - receive daily weather-based home care tips
                - manage bills, purchase invoices, subscriptions, and payments

                DAR does not:
                - provide technicians
                - perform maintenance
                - repair devices
                - answer medical, legal, political, school homework, or unrelated questions

                User question:
                %s

                Instructions:
                - Answer in Arabic only.
                - Keep the answer short and helpful.
                - Answer only if the question is related to DAR, home care, home maintenance, reminders, weather alerts, bills, invoices, subscriptions, or payments.
                - If the question is outside DAR scope, answer exactly:
                  عذرًا، استفسارك خارج نطاقي.
                """.formatted(question);

        return openAIService.generateReaderAnalysis(prompt);
    }
}
