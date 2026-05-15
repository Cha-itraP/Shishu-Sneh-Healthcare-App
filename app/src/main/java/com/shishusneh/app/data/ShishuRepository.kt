package com.shishusneh.app.data

import com.shishusneh.app.data.model.*

object ShishuRepository {

    val baby = Baby()

    val vaccines = listOf(
        Vaccine(1, "BCG", "Tuberculosis", "01 Apr 2026", VaccineStatus.OVERDUE),
        Vaccine(2, "OPV-0 (Polio)", "Polio", "01 Apr 2026", VaccineStatus.OVERDUE),
        Vaccine(3, "Hepatitis B — Birth", "Hepatitis B", "13 May 2026", VaccineStatus.UPCOMING),
        Vaccine(4, "Pentavalent-1", "DPT + Hib + Hep B", "13 May 2026", VaccineStatus.UPCOMING)
    )

    val milestones = mutableListOf(
        Milestone(1, "Focuses on faces", "Baby looks at your face when held close", "14d", MilestoneCategory.COGNITIVE, MilestoneStatus.ACHIEVED),
        Milestone(2, "Responds to sound", "Baby startles or turns toward sounds", "14d", MilestoneCategory.LANGUAGE, MilestoneStatus.ACHIEVED),
        Milestone(3, "Lifts head briefly", "When on tummy, lifts head slightly", "4w", MilestoneCategory.MOTOR, MilestoneStatus.ACHIEVED),
        Milestone(4, "Social smile", "Baby smiles in response to your smile", "6w", MilestoneCategory.SOCIAL, MilestoneStatus.ACHIEVED),
        Milestone(5, "Coos and makes sounds", "Baby makes soft vowel sounds", "6w", MilestoneCategory.LANGUAGE, MilestoneStatus.PENDING),
        Milestone(6, "Holds head steady", "Can hold head upright for several seconds", "2m", MilestoneCategory.MOTOR, MilestoneStatus.PENDING)
    )

    val feedingEntries = listOf(
        FeedingEntry(1, FeedingType.BREASTFEED, "Right breast", "400 ml", "25 min", "01:00 am"),
        FeedingEntry(2, FeedingType.BOTTLE, "Formula", "500 ml", "15 min", "04:00 am"),
        FeedingEntry(3, FeedingType.BREASTFEED, "Left breast", "380 ml", "20 min", "08:00 am")
    )

    /**
     * Baseline growth data — birthValue is the only known measurement at signup.
     * currentValue, percentile, chartPoints and entries are intentionally empty.
     * Real measurements are added by the user via AddGrowthDialog and stored in Room.
     */
    val growthDataMap = mapOf(
        GrowthMetric.WEIGHT to GrowthData(
            metric       = GrowthMetric.WEIGHT,
            birthValue   = "—",
            currentValue = "—",
            percentile   = "—",
            chartPoints  = emptyList(),
            entries      = emptyList()
        ),
        GrowthMetric.HEIGHT to GrowthData(
            metric       = GrowthMetric.HEIGHT,
            birthValue   = "—",
            currentValue = "—",
            percentile   = "—",
            chartPoints  = emptyList(),
            entries      = emptyList()
        ),
        GrowthMetric.HEAD to GrowthData(
            metric       = GrowthMetric.HEAD,
            birthValue   = "—",
            currentValue = "—",
            percentile   = "—",
            chartPoints  = emptyList(),
            entries      = emptyList()
        )
    )

    val guideCards = listOf(
        GuideCard("breastfeed", "Breastfeeding", "Every feeding is a chance to connect — gently, calmly, and with care.", "🤱", "green"),
        GuideCard("home", "Bringing Baby Home", "Set up a calm and loving space to welcome your baby into your arms.", "🏠", "pink"),
        GuideCard("self", "Taking Care of Yourself", "Your well-being matters — a rested parent is a better parent.", "🧘", "lavender"),
        GuideCard("sleep", "Baby Sleep Basics", "Help your newborn sleep safely and develop healthy sleep habits.", "😴", "yellow"),
        GuideCard("growth", "Growth & Development", "Track what to expect week by week in your baby's development.", "📈", "blue"),
        GuideCard("nutrition", "Mama's Nutrition", "What you eat shapes your milk — nourish yourself to nourish your baby.", "🥗", "peach")
    )

    val guideDetails = mapOf(
        "breastfeed" to GuideDetail(
            id = "breastfeed", title = "Breastfeeding", subtitle = "Latch well, feed often",
            bannerTitle = "Fed is best 🤱", bannerBody = "Exclusive breastfeeding for 6 months is recommended by WHO. Your body adjusts to your baby's needs.",
            bannerEmoji = "🌸",
            tips = listOf(
                GuideTip("TIP 01", "Latch deeply", "Ensure baby's mouth covers the areola, not just the nipple. A good latch prevents pain and ensures adequate milk transfer."),
                GuideTip("TIP 02", "Feed on demand", "Newborns feed 8–12 times a day. Don't watch the clock — feed whenever baby shows hunger cues."),
                GuideTip("TIP 03", "Watch for hunger cues", "Rooting, sucking hands, and turning head are early hunger signs. Crying is a late hunger cue."),
                GuideTip("TIP 04", "Both breasts", "Offer both breasts each feeding. Start on the side you ended last time for balanced supply.")
            )
        ),
        "home" to GuideDetail(
            id = "home", title = "Bringing Baby Home", subtitle = "Prepare a safe, warm space",
            bannerTitle = "Safe sleep setup 🏠", bannerBody = "A firm, flat surface with no loose bedding is the safest place for baby to sleep.",
            bannerEmoji = "🛏️",
            tips = listOf(
                GuideTip("TIP 01", "Room temperature", "Keep baby's room at 68–72°F (20–22°C). Overheating increases SIDS risk."),
                GuideTip("TIP 02", "Skin-to-skin", "Kangaroo care regulates temperature, heart rate, and promotes bonding and breastfeeding."),
                GuideTip("TIP 03", "Limit visitors", "In the first 2 weeks, limit visitors to reduce infection risk. Hand-washing is mandatory."),
                GuideTip("TIP 04", "Newborn hearing & sight", "Baby can see 8–12 inches — perfect for feeding distance. They recognize your voice from birth.")
            )
        ),
        "self" to GuideDetail(
            id = "self", title = "Taking Care of Yourself", subtitle = "You matter too",
            bannerTitle = "Rest is not a luxury 🧘", bannerBody = "Postpartum recovery takes 6–8 weeks. Accept help, sleep when baby sleeps, and nourish your body.",
            bannerEmoji = "💜",
            tips = listOf(
                GuideTip("TIP 01", "Baby blues vs PPD", "Feeling emotional for 2 weeks is normal. If sadness persists beyond 2 weeks, speak to your doctor about postpartum depression."),
                GuideTip("TIP 02", "Accept help", "Let family help with chores, cooking, and older siblings. Your only job is to feed and bond with baby."),
                GuideTip("TIP 03", "Gentle movement", "A 10-minute walk improves mood. Avoid strenuous exercise until your 6-week checkup."),
                GuideTip("TIP 04", "Connect with others", "Join a new-parent group, online or local. Shared experience reduces isolation dramatically.")
            )
        ),
        "sleep" to GuideDetail(
            id = "sleep", title = "Baby Sleep Basics", subtitle = "Safe sleep, healthy habits",
            bannerTitle = "Back is best 😴", bannerBody = "Always place baby on their back to sleep. This is the single most important step to reduce SIDS risk.",
            bannerEmoji = "🌙",
            tips = listOf(
                GuideTip("WEEK 1–2", "No schedule yet", "Newborns sleep 16–18 hrs in short 2–4 hr cycles. This is normal — no schedule needed yet."),
                GuideTip("WEEK 3–4", "First growth spurt", "Baby may feed more, seem fussier, and sleep more. Growth spurts last 2–3 days. Feed on demand."),
                GuideTip("WEEK 5–6", "Social smiles emerge", "Your baby may start smiling in response to your face. This is a huge social milestone — smile back!")
            )
        ),
        "growth" to GuideDetail(
            id = "growth", title = "Growth & Development", subtitle = "Week by week progress",
            bannerTitle = "Every baby is unique 📈", bannerBody = "Growth charts show ranges, not targets. Your baby's own growth trend matters more than any single number.",
            bannerEmoji = "🌱",
            tips = listOf(
                GuideTip("WEEK 1", "Weight loss is normal", "Babies lose up to 10% of birth weight in the first week and regain it by day 10–14."),
                GuideTip("WEEK 2–4", "Head control begins", "Tummy time helps strengthen neck and shoulder muscles for future motor milestones."),
                GuideTip("MONTH 2", "Social awareness", "Baby starts tracking faces with their eyes and responding with coos and smiles."),
                GuideTip("MONTH 3", "Hand discovery", "Baby begins batting at objects and may bring hands to mouth — exploring the world begins!")
            )
        ),
        "nutrition" to GuideDetail(
            id = "nutrition", title = "Mama's Nutrition", subtitle = "Eat well to feed well",
            bannerTitle = "Nourish yourself 🥗", bannerBody = "What you eat directly shapes the quality and quantity of your breast milk.",
            bannerEmoji = "🌾",
            tips = listOf(
                GuideTip("TIP 01", "Galactagogues (milk boosters)", "Include methi (fenugreek), oats, moringa, nuts, and ajwain. These traditional foods are backed by generations of use."),
                GuideTip("TIP 02", "Extra calories needed", "Breastfeeding requires an extra 300–500 calories/day. Focus on nutrient-dense foods like lentils, eggs, and leafy greens."),
                GuideTip("TIP 03", "Hydrate generously", "Drink 8–10 glasses of water daily. Milk, coconut water, and herbal teas also help."),
                GuideTip("TIP 04", "Iron & calcium are key", "Continue prenatal vitamins. Eat ragi, sesame seeds, and green vegetables for calcium.")
            )
        )
    )

    val notifications = listOf(
        Notification(1, "VACCINE", "vax", "BCG vaccination is overdue", "Due since 01 Apr 2026 · Visit nearest clinic", "💉"),
        Notification(2, "TIP", "tip", "Talk and sing often to your baby", "Your voice builds early language and emotional security", "🌸"),
        Notification(3, "REMINDER", "reminder", "Polio drops due soon", "OPV-0 scheduled · Don't miss this important shot", "💊"),
        Notification(4, "TIP", "tip", "Tummy time today", "2–3 minutes of tummy time builds neck strength", "🐣"),
        Notification(5, "VACCINE", "vax", "Hepatitis B due 13 May 2026", "Birth dose · Prevents Hepatitis B infection", "💉")
    )

    val todayTip = "🌸 Talk and sing often. Your voice is the most comforting sound for your baby and builds early language."
    val vaccinationEventDays = listOf(9, 13, 20) // days in current month with events
}
