package com.example.data

import android.content.Context
import android.content.SharedPreferences
import com.example.domain.RecommendationEngine
import com.example.domain.UserProfileQuery
import com.example.model.*
import org.json.JSONArray
import org.json.JSONObject

class HustleRepository(context: Context) {

    private val prefs: SharedPreferences =
        context.getSharedPreferences("biashara_hub_prefs", Context.MODE_PRIVATE)

    val defaultSkills: List<Skill> = listOf(
        // Digital & Tech
        Skill("graphic_design", "Graphic Design & Canva", SkillCategory.DIGITAL_TECH, "Creating posters, logos, flyers & social banners"),
        Skill("social_media", "Social Media Management", SkillCategory.DIGITAL_TECH, "Managing Instagram/TikTok pages & running Meta ads"),
        Skill("phone_repair", "Mobile Phone Repair", SkillCategory.DIGITAL_TECH, "Fixing cracked screens, charging ports & software flashing"),
        Skill("photo_video", "Photography & Video Editing", SkillCategory.DIGITAL_TECH, "Shooting reels, events, product photos & CapCut editing"),
        Skill("transcription", "Data Entry & eCitizen Services", SkillCategory.DIGITAL_TECH, "Typing, printing, KRA returns & passport applications"),
        Skill("web_dev", "Basic Web Design & WordPress", SkillCategory.DIGITAL_TECH, "Building business portfolio sites & landing pages"),

        // Vocational & Trades
        Skill("carpentry", "Carpentry & Pallet Woodwork", SkillCategory.VOCATIONAL_TRADES, "Building coffee tables, shoe racks & planter boxes"),
        Skill("plumbing_elec", "Electrical & Plumbing Repairs", SkillCategory.VOCATIONAL_TRADES, "Wiring, socket installation & piping maintenance"),
        Skill("welding", "Welding & Metal Fabrication", SkillCategory.VOCATIONAL_TRADES, "Gate repairs, window grills & charcoal jiko frames"),
        Skill("tailoring", "Tailoring & Clothing Alterations", SkillCategory.VOCATIONAL_TRADES, "Resizing thrift clothes, sewing cushions & dresses"),

        // Agri-business & Farming
        Skill("poultry", "Poultry & Kienyeji Keeping", SkillCategory.AGRIBUSINESS, "Brooding chicks, egg production & feed mixing"),
        Skill("horticulture", "Vegetables & Hydroponics", SkillCategory.AGRIBUSINESS, "Growing spinach, sukuma wiki, capsicum & tomatoes"),
        Skill("beekeeping", "Beekeeping & Honey Harvesting", SkillCategory.AGRIBUSINESS, "Managing beehives and pure raw honey packaging"),

        // Food & Culinary
        Skill("cooking_baking", "Cooking & Snack Preparation", SkillCategory.FOOD_HOSPITALITY, "Making samosas, mandazi, smokie pasua & chapatis"),
        Skill("pastry_cakes", "Cake Baking & Pastry Decor", SkillCategory.FOOD_HOSPITALITY, "Baking birthday cakes, banana bread & muffins"),
        Skill("beverages", "Fresh Juice & Herbal Blends", SkillCategory.FOOD_HOSPITALITY, "Blending sugar cane, sugarcane/ginger & smoothies"),

        // Services & Logistics
        Skill("boda_riding", "Boda Boda Riding & Delivery", SkillCategory.SERVICES_LOGISTICS, "Navigating estate streets, parcel handling & prompt delivery"),
        Skill("car_wash", "Car Wash & Auto Detailing", SkillCategory.SERVICES_LOGISTICS, "Pressure washing, engine bay degreasing & interior vacuuming"),
        Skill("cleaning", "Sofa & Carpet Cleaning", SkillCategory.SERVICES_LOGISTICS, "Shampoo extraction, mattress sanitization & stain removal"),
        Skill("events_decor", "Events Setup & Sound Decor", SkillCategory.SERVICES_LOGISTICS, "Balloon arches, drapery, PA system rental & MC duties"),

        // Beauty & Fashion
        Skill("barbering", "Barbering & Hair Fades", SkillCategory.BEAUTY_FASHION, "Crisp hair lining, fading, beard grooming & dye"),
        Skill("hair_styling", "Braiding & Dreadlocks", SkillCategory.BEAUTY_FASHION, "Knotless braids, dreadlock relocking & wig styling"),
        Skill("nail_art", "Nail Tech & Pedicures", SkillCategory.BEAUTY_FASHION, "Acrylic nails, gel polish & foot care"),

        // Retail & Chemical Formulations
        Skill("detergent_making", "Liquid Soap & Bleach Making", SkillCategory.RETAIL_COMMERCE, "Formulating multipurpose detergent, disinfectant & bar soaps"),
        Skill("thrift_curation", "Mitumba Selection & Ironing", SkillCategory.RETAIL_COMMERCE, "Picking camera grade clothes at Gikomba & steam pressing"),
        Skill("perfume_blending", "Perfume & Fragrance Refilling", SkillCategory.RETAIL_COMMERCE, "Mixing concentrated oils and stylish spray bottling")
    )

    val businessIdeas: List<BusinessIdea> = listOf(
        BusinessIdea(
            id = "mitumba_thrift",
            title = "Curated Instagram & Estate Mitumba Thrift",
            shengTitle = "Kamera Thrift Store",
            category = SkillCategory.RETAIL_COMMERCE,
            interestArea = InterestArea.FASHION_BEAUTY,
            summary = "Source high-grade first selection (Camera) garments from Gikomba or Toi, steam iron, photograph cleanly, and sell via Instagram/WhatsApp or estate stalls.",
            problemBeingSolved = "Young professionals and students want trendy, unique fashion without paying exorbitant prices in high-end malls.",
            proposedSolution = "Hand-selected, steam-pressed, and accurately sized camera-grade mitumba delivered right to their estate gate.",
            targetCustomers = listOf("University students", "Young working women", "Estate WhatsApp fashion shoppers"),
            matchedSkills = listOf("thrift_curation", "social_media", "photo_video", "tailoring"),
            capitalMinKes = 4000,
            capitalMaxKes = 12000,
            capitalTier = CapitalTier.LOW,
            capitalBreakdown = mapOf(
                "Initial Camera Grade Stock (Gikomba)" to 5000,
                "Handheld Garment Steamer" to 2200,
                "Packaging bags & branding stickers" to 1000,
                "Data bundles & Social ads boost" to 800
            ),
            estimatedDailyRevenueKes = 1500..4500,
            estimatedMonthlyProfitKes = 25000..55000,
            paybackPeriod = "3 - 5 weeks",
            idealLocations = listOf(EnvironmentType.ONLINE, EnvironmentType.PERI_URBAN, EnvironmentType.URBAN),
            businessModel = BusinessModelType.HYBRID,
            riskLevel = RiskLevel.LOW,
            timeCommitment = TimeCommitment.FLEXIBLE,
            requiredEquipmentIds = listOf("smartphone"),
            equipmentNeeded = listOf("Smartphone with decent camera", "Garment Steamer or Flat Iron", "Mannequin torso or clean hanger", "Custom courier dispatch bags"),
            supplierAndSourcingGuidance = "Gikomba Market (Stage 4 or Grogon) between 5:30 AM - 7:00 AM on Tuesdays and Fridays for fresh bale opening.",
            pricingConsiderations = "Acquire at KSh 150-250 per piece; clean, press, and price between KSh 600 - 1,200 depending on brand and rarity.",
            marketingStrategy = "Short TikTok styling reels, Instagram carousels with chest/waist measurements in inches, and estate WhatsApp status flash sales.",
            competitorResearchGuidance = "Benchmark existing Nairobi thrift pages like @nairobithrift; focus on faster delivery and flexible MPESA returns.",
            launchSteps = listOf(
                "Visit Gikomba Market at 5:30 AM on Tuesday or Friday for bale opening.",
                "Handpick 25-30 premium jackets, trench coats, or dresses.",
                "Wash, stain-treat, and steam iron every item to look brand new.",
                "Shoot daylight photos against a neutral background and note exact measurements.",
                "Post on Instagram Reels & TikTok; dispatch via local riders or Fargo Courier."
            ),
            compliance = listOf(
                ComplianceRequirement("Business Name Registration", "eCitizen BRS", "KSh 950", "Protects your brand name on Kenyan national registry", false),
                ComplianceRequirement("Single Business Permit", "County Government", "KSh 3,500 - 7,000", "Only required if operating a physical shop or stall", false)
            ),
            mpesaTip = "Activate Pochi La Biashara (*334#) to accept zero-fee customer payments while keeping personal savings segregated.",
            insiderTips = listOf(
                "Niche down! Specialize in activewear, vintage leather jackets, or baby clothes rather than generic outfits.",
                "Always list armpit-to-armpit (bust) measurements in inches to reduce return disputes."
            ),
            risksAndMitigations = listOf(
                "Slow initial sales" to "Offer free local doorstep delivery to nearby estates and run giveaways for shares.",
                "Customer cancellation on delivery" to "Collect KSh 200 commitment fee before sending the courier."
            ),
            lastReviewedDate = "October 2026"
        ),

        BusinessIdea(
            id = "smokie_pasua",
            title = "Smokie Pasua, Boiled Eggs & Kachumbari Cart",
            shengTitle = "Kitoro ya Mayai & Pasua",
            category = SkillCategory.FOOD_HOSPITALITY,
            interestArea = InterestArea.FOOD_BEVERAGE,
            summary = "High-margin, daily cash flow business serving fresh boiled eggs, Farmer's Choice smokies, and spicy homemade kachumbari at busy transit points.",
            problemBeingSolved = "Hungry commuters, students, and night workers need affordable, hot, hygienic fast food during transit hours.",
            proposedSolution = "Quick-serve boiled eggs (KSh 25) and smokie pasua (KSh 35) paired with fresh, zesty tomato-onion kachumbari.",
            targetCustomers = listOf("Matatu commuters", "Boda riders", "Campus students", "Night shift workers"),
            matchedSkills = listOf("cooking_baking", "beverages"),
            capitalMinKes = 5000,
            capitalMaxKes = 12000,
            capitalTier = CapitalTier.LOW,
            capitalBreakdown = mapOf(
                "Modified Wheeled Trolley with steamer" to 4500,
                "Smokies crate (Farmer's Choice) + 2 crates of eggs" to 3200,
                "Tomatoes, onions, chili, salt & vinegar stock" to 800,
                "Food handling health cert & waste bucket" to 1500
            ),
            estimatedDailyRevenueKes = 2500..5000,
            estimatedMonthlyProfitKes = 30000..60000,
            paybackPeriod = "3 - 4 weeks",
            idealLocations = listOf(EnvironmentType.URBAN, EnvironmentType.PERI_URBAN),
            businessModel = BusinessModelType.OFFLINE,
            riskLevel = RiskLevel.LOW,
            timeCommitment = TimeCommitment.PART_TIME,
            requiredEquipmentIds = listOf("kitchen"),
            equipmentNeeded = listOf("Insulated stainless steel or glass trolley", "Sharp pairing knives", "Condiment containers for kachumbari", "Tongs & disposable napkins"),
            supplierAndSourcingGuidance = "Purchase eggs wholesale from Nyamakima or directly from peri-urban poultry farmers at KSh 380/crate vs KSh 450 in retail shops.",
            pricingConsiderations = "Sell eggs at KSh 25 (cost is KSh 12.60), smokies at KSh 35 (cost is KSh 20). Combo at KSh 55 for quick high-volume conversion.",
            marketingStrategy = "Location is 90% of your marketing: position beside matatu stages, busy footbridges, or outside campus hostels.",
            competitorResearchGuidance = "Observe queue lengths at neighbouring carts; differentiate with cleaner presentation and fresh kachumbari.",
            launchSteps = listOf(
                "Scout a high-footfall location: Stage ya matatu or campus gate.",
                "Acquire a clean trolley from Gikomba or local welders for KSh 3,500-4,500.",
                "Source eggs at wholesale (KSh 380/crate) and genuine Farmer's Choice smokies.",
                "Boil eggs with a splash of vinegar for effortless peeling; prepare clean kachumbari.",
                "Operate during peak evening hours (4:30 PM to 9:30 PM)."
            ),
            compliance = listOf(
                ComplianceRequirement("Food Handlers Medical Certificate", "County Public Health", "KSh 1,000", "Annual medical examination and yellow booklet", true),
                ComplianceRequirement("Hawker/Street Vendor Sticker", "Sub-County Revenue", "KSh 50-100/day or KSh 2,500/yr", "Permits trading in designated zones", true)
            ),
            mpesaTip = "Laminate a large Pochi La Biashara QR code so customers tap and go without loose coin hassles.",
            insiderTips = listOf(
                "Never let kachumbari sit for hours in the sun; prepare small fresh batches every 2 hours.",
                "Friendly banter and hygiene (clean white apron) will make your trolley the estate favourite."
            ),
            risksAndMitigations = listOf(
                "County askari (Kanjo) disruption" to "Partner with a shop veranda or obtain the official daily revenue sticker.",
                "Rainy evenings" to "Mount a sturdy waterproof umbrella to protect your cart and waiting customers."
            ),
            lastReviewedDate = "October 2026"
        ),

        BusinessIdea(
            id = "graphics_sme",
            title = "Digital Branding & Social Media for SMEs",
            shengTitle = "Canva & Brand Fundi",
            category = SkillCategory.DIGITAL_TECH,
            interestArea = InterestArea.ONLINE_FREELANCING,
            summary = "Help local salons, hardware stores, chemists, and food joints create eye-catching price menus, logo designs, posters, and daily TikTok/Instagram posts.",
            problemBeingSolved = "Small Kenyan business owners know they need social media sales but lack the time and graphic skills to create professional promotional material.",
            proposedSolution = "Done-for-you monthly graphic design and WhatsApp status catalogue updates at affordable SME retainer rates.",
            targetCustomers = listOf("Neighbourhood beauty salons", "Hardware stores", "Private medical clinics", "Local boutique owners"),
            matchedSkills = listOf("graphic_design", "social_media", "photo_video", "web_dev"),
            capitalMinKes = 2500,
            capitalMaxKes = 8000,
            capitalTier = CapitalTier.MICRO,
            capitalBreakdown = mapOf(
                "Canva Pro / Mobile design subscription" to 1400,
                "Safaricom monthly mobile data bundle" to 2000,
                "Sample portfolio printouts" to 600
            ),
            estimatedDailyRevenueKes = 1200..3500,
            estimatedMonthlyProfitKes = 35000..75000,
            paybackPeriod = "2 weeks",
            idealLocations = listOf(EnvironmentType.ONLINE, EnvironmentType.PERI_URBAN, EnvironmentType.URBAN),
            businessModel = BusinessModelType.ONLINE,
            riskLevel = RiskLevel.LOW,
            timeCommitment = TimeCommitment.FLEXIBLE,
            requiredEquipmentIds = listOf("smartphone"),
            equipmentNeeded = listOf("Smartphone or refurbished laptop", "Canva / Adobe Express / CapCut app", "Fast 4G/WiFi internet connection"),
            supplierAndSourcingGuidance = "All digital tools; utilize Canva Pro educational discounts or official monthly subscriptions.",
            pricingConsiderations = "Charge KSh 5,000 - 8,000 per month per business for 12 social posts + 2 video reels. 6 clients = KSh 36,000 predictable income.",
            marketingStrategy = "Walk into local shops, redesign their faded menu or poster for free as a proof-of-concept, and show them the comparison.",
            competitorResearchGuidance = "Most big agencies ignore businesses with sub-KSh 10,000 budgets—that is your untapped goldmine.",
            launchSteps = listOf(
                "Design 3 compelling sample price menus and flyers for local businesses.",
                "Visit local business owners and demonstrate how a crisp graphic boosts WhatsApp customer trust.",
                "Sign up your first 3 clients on a KSh 4,500/month trial retainer.",
                "Deliver scheduled graphics each week and monitor customer response.",
                "Request word-of-mouth referrals and expand into TikTok video edits."
            ),
            compliance = listOf(
                ComplianceRequirement("Individual KRA PIN", "KRA iTax Portal", "Free", "Required for invoice generation and tax returns", false),
                ComplianceRequirement("Business Name Registration", "eCitizen BRS", "KSh 950", "Official business certificate for bank accounts", false)
            ),
            mpesaTip = "Send professional PDF quotes with your Lipa Na M-PESA Till number embedded at the bottom.",
            insiderTips = listOf(
                "Include clear WhatsApp call-to-action buttons with phone numbers on every single design.",
                "Take photos of real products inside the client's shop using portrait mode instead of generic stock images."
            ),
            risksAndMitigations = listOf(
                "Delayed client payments" to "Collect 50% deposit before design kickoff and balance prior to raw file dispatch.",
                "Device breakdown" to "Backup all project templates to Google Drive for multi-device access."
            ),
            lastReviewedDate = "October 2026"
        ),

        BusinessIdea(
            id = "mobile_carwash",
            title = "Doorstep Mobile Car Wash & Interior Detailing",
            shengTitle = "Mobile Sparkle Wash",
            category = SkillCategory.SERVICES_LOGISTICS,
            interestArea = InterestArea.CLEANING_MAINTENANCE,
            summary = "Bring washing directly to estates, offices, and residential compounds using portable 12V pressure washers and water-saving foam shampoo.",
            problemBeingSolved = "Vehicle owners waste 2-3 hours queuing at commercial car washes every weekend instead of relaxing with family.",
            proposedSolution = "Scheduled doorstep pressure washing and seat shampooing inside residential compounds while the owner relaxes at home.",
            targetCustomers = listOf("Apartment compound residents", "Busy professionals", "Taxi/Uber fleet owners"),
            matchedSkills = listOf("car_wash", "cleaning", "boda_riding"),
            capitalMinKes = 8500,
            capitalMaxKes = 22000,
            capitalTier = CapitalTier.LOW,
            capitalBreakdown = mapOf(
                "Portable 12V/Electric Pressure Washer" to 7500,
                "Foam cannon & microfiber towels pack" to 2200,
                "Car wash shampoo, tire shine & dashboard wax" to 2500,
                "Transport cart or bicycle rack" to 3500
            ),
            estimatedDailyRevenueKes = 2000..6000,
            estimatedMonthlyProfitKes = 40000..85000,
            paybackPeriod = "1 - 2 months",
            idealLocations = listOf(EnvironmentType.PERI_URBAN, EnvironmentType.URBAN),
            businessModel = BusinessModelType.OFFLINE,
            riskLevel = RiskLevel.MEDIUM,
            timeCommitment = TimeCommitment.FULL_TIME,
            requiredEquipmentIds = listOf("hand_tools"),
            equipmentNeeded = listOf("High-pressure portable washer", "30-meter extension cable", "Water drum & bucket", "Interior dry-wet vacuum"),
            supplierAndSourcingGuidance = "Purchase pressure washers along Kirinyaga Road or River Road; buy shampoo and tire shine in 20L bulk jerrycans.",
            pricingConsiderations = "Charge KSh 400 for saloons, KSh 600 for SUVs (body + interior vacuum). Offer KSh 1,500 monthly 4-wash subscription.",
            marketingStrategy = "Introduce yourself to estate caretakers and post professional before-and-after photos in estate WhatsApp groups.",
            competitorResearchGuidance = "Static car washes charge KSh 300-400 but force the client to travel; your convenience justifies premium pricing.",
            launchSteps = listOf(
                "Acquire a portable pressure washer and test on your family/friend's car.",
                "Print 50 simple flyers and speak with apartment caretakers in gated estates.",
                "Offer 5 complimentary car washes to estate chairpersons to build social proof.",
                "Sign up 10 regular clients for weekly Saturday and Sunday morning slots.",
                "Add interior seat steam shampooing as a KSh 1,500 deep-clean upsell."
            ),
            compliance = listOf(
                ComplianceRequirement("County Mobile Service Permit", "County Revenue", "KSh 3,000", "Covers mobile on-demand service provision", false),
                ComplianceRequirement("Estate Caretaker Approval", "Resident Association", "Free / Small token", "Permission to operate on compound grounds", false)
            ),
            mpesaTip = "Set up automated SMS payment receipts with your Pochi La Biashara account to look professional.",
            insiderTips = listOf(
                "Sundays are peak cash days: wash 8-10 cars in one gated court without changing locations.",
                "A glistening tire shine finish creates the strongest visual impact for car owners."
            ),
            risksAndMitigations = listOf(
                "Water shortages" to "Carry a 60L drum filled from reliable borehole taps.",
                "Vehicle damage disputes" to "Take 4 timestamped photos of the vehicle exterior before beginning."
            ),
            lastReviewedDate = "October 2026"
        ),

        BusinessIdea(
            id = "poultry_kienyeji",
            title = "Improved Kienyeji Poultry & Egg Production",
            shengTitle = "Kuku Kienyeji Farm",
            category = SkillCategory.AGRIBUSINESS,
            interestArea = InterestArea.FARMING_LIVESTOCK,
            summary = "Raise Kuroiler, Rainbow Rooster, or KALRO improved Kienyeji chickens for high-demand indigenous eggs and organic meat with strong resistance to diseases.",
            problemBeingSolved = "Kenyan urban consumers increasingly avoid broiler chicken meat with growth hormones, creating massive unmet demand for authentic Kienyeji chicken and eggs.",
            proposedSolution = "Semi-intensive improved Kienyeji poultry rearing with strict biosecurity, high egg production, and direct farm-to-table delivery.",
            targetCustomers = listOf("Health-conscious urban families", "Local restaurants and cafes", "Other farmers seeking fertilized eggs and chicks"),
            matchedSkills = listOf("poultry", "horticulture"),
            capitalMinKes = 9000,
            capitalMaxKes = 25000,
            capitalTier = CapitalTier.LOW,
            capitalBreakdown = mapOf(
                "50 Day-Old Vaccinated Chicks (KALRO/Kenchic)" to 5000,
                "Brooder heating bulb & wire mesh structure" to 3500,
                "Starter mash feed & essential vaccines" to 4200,
                "Drinkers and feeders" to 1500
            ),
            estimatedDailyRevenueKes = 1200..4000,
            estimatedMonthlyProfitKes = 35000..70000,
            paybackPeriod = "3 - 4 months",
            idealLocations = listOf(EnvironmentType.RURAL, EnvironmentType.PERI_URBAN),
            businessModel = BusinessModelType.HYBRID,
            riskLevel = RiskLevel.MEDIUM,
            timeCommitment = TimeCommitment.FULL_TIME,
            requiredEquipmentIds = listOf("land_space"),
            equipmentNeeded = listOf("Ventilated chicken coop / shed", "Infrared heat lamps or charcoal brooder", "Automated bell drinkers", "Disinfection footbath"),
            supplierAndSourcingGuidance = "Acquire certified day-old chicks from KALRO Naivasha/Kibos or Kenchic. Buy chick mash from reputable millers.",
            pricingConsiderations = "Table eggs sell at KSh 550 - 650 per tray. Mature cockerels for meat sell at KSh 1,000 - 1,300 each.",
            marketingStrategy = "Document weekly flock growth on TikTok and WhatsApp Status; pre-sell trays to colleagues and estate neighbours.",
            competitorResearchGuidance = "Broiler chicken is cheaper, but Kienyeji commands a 100% price premium due to taste and organic health perceptions.",
            launchSteps = listOf(
                "Construct a predator-proof coop with adequate ventilation and wood shavings.",
                "Order 50 day-old chicks from KALRO or Kenchic.",
                "Follow a strict vaccination calendar: Mareks, Gumboro (Day 7/14), Newcastle (Day 21).",
                "Supplement commercial feed with hydroponic greens (barley) and azolla to cut feeding costs by 30%.",
                "Sell mature cockerels at 4.5 months and harvest eggs from layers."
            ),
            compliance = listOf(
                ComplianceRequirement("Veterinary Health Movement Permit", "Sub-County Livestock Office", "KSh 500", "Needed when transporting live birds across county borders", false),
                ComplianceRequirement("Local Agricultural Officer Advisory", "Ministry of Agriculture", "Free", "Free biosecurity and vaccination advisory", false)
            ),
            mpesaTip = "Collect 50% M-PESA deposits before slaughtering or dispatching eggs to avoid order cancellations.",
            insiderTips = listOf(
                "Biosecurity is survival: restrict chicken coop visits to prevent Newcastle and fowl typhoid transmission.",
                "Fertilized kienyeji eggs sell for KSh 800-1,000/tray to hatchery incubator operators—much higher than table eggs!"
            ),
            risksAndMitigations = listOf(
                "Disease outbreak" to "Pin vaccination timetable on coop door and disinfect footbath daily.",
                "Feed price inflation" to "Grow black soldier fly (BSF) larvae or azolla for free high-protein feed."
            ),
            lastReviewedDate = "October 2026"
        ),

        BusinessIdea(
            id = "detergent_liquid_soap",
            title = "Multi-Purpose Liquid Detergent & Bleach Production",
            shengTitle = "Sabuni ya Mtungi Hub",
            category = SkillCategory.RETAIL_COMMERCE,
            interestArea = InterestArea.CLEANING_MAINTENANCE,
            summary = "Produce and sell 5-liter and 20-liter multi-purpose liquid soap, hand wash, and disinfectant to car washes, schools, laundromats, and homes.",
            problemBeingSolved = "Commercial supermarket detergents (Omo, Sunlight) are increasingly expensive for budget-conscious Kenyan families and local businesses.",
            proposedSolution = "High-foaming, fresh-fragrance multi-purpose liquid detergent formulated locally and sold in refillable 5L/20L jerrycans at half commercial prices.",
            targetCustomers = listOf("Commercial car washes", "Estate households & chamas", "Laundromats & butcheries", "Local primary schools"),
            matchedSkills = listOf("detergent_making", "retail_commerce"),
            capitalMinKes = 3500,
            capitalMaxKes = 10000,
            capitalTier = CapitalTier.MICRO,
            capitalBreakdown = mapOf(
                "Chemical raw kit for 100 Litres (SLES, Ufacid, Salt, Perfume, Color)" to 2800,
                "100L Mixing drum & wooden paddle" to 1500,
                "Recycled 5L Jerrycans (20 units)" to 1000,
                "Branding stickers" to 500
            ),
            estimatedDailyRevenueKes = 1500..4000,
            estimatedMonthlyProfitKes = 28000..60000,
            paybackPeriod = "2 - 3 weeks",
            idealLocations = listOf(EnvironmentType.PERI_URBAN, EnvironmentType.RURAL, EnvironmentType.URBAN),
            businessModel = BusinessModelType.OFFLINE,
            riskLevel = RiskLevel.LOW,
            timeCommitment = TimeCommitment.PART_TIME,
            requiredEquipmentIds = listOf("hand_tools"),
            equipmentNeeded = listOf("100L plastic drum", "Measuring scale and funnel", "Long wooden mixing stick", "Protective gloves and goggles"),
            supplierAndSourcingGuidance = "Purchase pre-measured chemical packs from River Road or industrial chemical distributors in county town centers.",
            pricingConsiderations = "Production cost is KSh 110 per 5L jerrycan. Sell to car washes at KSh 220 wholesale and households at KSh 250 (over 100% gross profit!).",
            marketingStrategy = "Provide free 500ml sample bottles to estate mama mbogas and car wash attendants on Monday morning.",
            competitorResearchGuidance = "Competitors often skimp on perfume or foam booster; maintain thick viscosity and vibrant pine or lemon aroma.",
            launchSteps = listOf(
                "Buy a 100L chemical kit from River Road chemical shops.",
                "Mix Sulphonic acid, SLES, industrial salt, color, and pine perfume in clean water.",
                "Allow foam to settle overnight to produce thick, sparkling clear soap.",
                "Pour into clean recycled 5L jerrycans with branded stickers.",
                "Deliver weekly refills to local car washes and estate neighbours."
            ),
            compliance = listOf(
                ComplianceRequirement("KEBS MSME Standardization Scheme", "KEBS SME Office", "Subsidized KSh 5,000", "Under youth cottage industry support initiative", false),
                ComplianceRequirement("Business Name Registration", "eCitizen BRS", "KSh 950", "Official registered product name", false)
            ),
            mpesaTip = "Offer a KSh 30 M-PESA discount when customers return their empty 5L jerrycans for a refill.",
            insiderTips = listOf(
                "Car washes and butcheries buy in bulk every Monday; set them up on a predictable weekly delivery cycle.",
                "Add pine disinfectant to expand your product catalog for schools and clinics."
            ),
            risksAndMitigations = listOf(
                "Soap thinning" to "Carefully weigh industrial salt; adding too much salt breaks the chemical chain and liquefies the soap.",
                "Skin irritation" to "Always wear protective rubber gloves during initial chemical blending."
            ),
            lastReviewedDate = "October 2026"
        )
    )

    val defaultJourneyTasks: List<JourneyTask> = listOf(
        // Stage 1: Discover
        JourneyTask("disc_1", "Review Launch Blueprint", "Read the complete capital breakdown, equipment checklist, and insider secrets.", JourneyStage.DISCOVER, true),
        JourneyTask("disc_2", "Profile Alignment Check", "Confirm your available skills, time commitment, and location match the business requirements.", JourneyStage.DISCOVER, true),

        // Stage 2: Evaluate
        JourneyTask("eval_1", "Run Profit & Cashflow Calculator", "Model your daily sales volume, selling price, and overhead in the Kikokotoo.", JourneyStage.EVALUATE, true),
        JourneyTask("eval_2", "Interview 3 Potential Customers", "Talk to 3 people in your estate or target audience to validate demand and pricing.", JourneyStage.EVALUATE, false),

        // Stage 3: Research
        JourneyTask("res_1", "Scout Wholesale Suppliers", "Visit the recommended wholesale market (e.g. Gikomba, Nyamakima, or River Road) to compare prices.", JourneyStage.RESEARCH, true),
        JourneyTask("res_2", "Competitor Spot Check", "Observe 2 existing competitors in your area to note their strengths, queues, and customer complaints.", JourneyStage.RESEARCH, false),

        // Stage 4: Prepare
        JourneyTask("prep_1", "Assemble Startup Capital", "Gather your savings or apply for Hustler Fund (*254#) / Chama loan to cover initial stock.", JourneyStage.PREPARE, true),
        JourneyTask("prep_2", "Acquire Primary Equipment", "Source the essential tools and packaging supplies needed before opening day.", JourneyStage.PREPARE, true),
        JourneyTask("prep_3", "Apply for Applicable Permits", "Obtain your food handling cert, county vendor sticker, or eCitizen business name.", JourneyStage.PREPARE, false),

        // Stage 5: Launch
        JourneyTask("launch_1", "Activate Pochi La Biashara / Till", "Dial *334# to set up your business payment channel so customer funds are segregated.", JourneyStage.LAUNCH, true),
        JourneyTask("launch_2", "Opening Day Promotion", "Fulfill your first 10 orders with exceptional customer service and collect feedback.", JourneyStage.LAUNCH, true),

        // Stage 6: Grow
        JourneyTask("grow_1", "Reinvest 50% Daily Profits", "Set aside half of your daily net earnings into expanding bulk inventory or tool upgrades.", JourneyStage.GROW, true),
        JourneyTask("grow_2", "Build Referral WhatsApp Group", "Create a VIP customer broadcast group to announce fresh stock arrivals and discounts.", JourneyStage.GROW, false)
    )

    val defaultDocuments: List<BusinessDocument> = listOf(
        BusinessDocument(
            id = "doc_msme_guide_2026",
            title = "Kenyan MSME Startup & Growth Blueprint 2026",
            description = "Comprehensive guide on bootstrapping micro-enterprises across Kenyan counties, navigating county permits, and managing cashflow.",
            category = DocumentCategory.STARTUP_GUIDES,
            fileType = "PDF",
            sizeBytes = 2450000,
            version = "2.1",
            isPremium = false,
            remoteR2ObjectKey = "guides/2026/kenyan_msme_startup_blueprint.pdf",
            downloadStatus = DownloadStatus.NOT_DOWNLOADED
        ),
        BusinessDocument(
            id = "doc_cashflow_worksheet",
            title = "Weekly Cashflow & Record-Keeping Ledger",
            description = "Printable daily revenue and expense tracker tailored for Kenyan kiosks, online thrift vendors, and service fundis.",
            category = DocumentCategory.FINANCIAL_SHEETS,
            fileType = "PDF",
            sizeBytes = 850000,
            version = "1.3",
            isPremium = false,
            remoteR2ObjectKey = "templates/cashflow_record_keeping_sheet.pdf",
            downloadStatus = DownloadStatus.NOT_DOWNLOADED
        ),
        BusinessDocument(
            id = "doc_compliance_checklist",
            title = "eCitizen, KRA & County Permit Formalization Roadmap",
            description = "Step-by-step walkthrough for registering a business name on eCitizen (BRS), getting a KRA PIN, and acquiring a single business permit.",
            category = DocumentCategory.LEGAL_COMPLIANCE,
            fileType = "PDF",
            sizeBytes = 1120000,
            version = "2.0",
            isPremium = false,
            remoteR2ObjectKey = "legal/ecitizen_kra_county_permit_guide.pdf",
            downloadStatus = DownloadStatus.NOT_DOWNLOADED
        ),
        BusinessDocument(
            id = "doc_mpesa_guide",
            title = "M-PESA Business Playbook (Pochi vs Till vs Paybill)",
            description = "Practical guide to setting up cashless payments without transaction fee losses, handling reconciliations, and avoiding reversals.",
            category = DocumentCategory.STARTUP_GUIDES,
            fileType = "PDF",
            sizeBytes = 980000,
            version = "1.5",
            isPremium = false,
            remoteR2ObjectKey = "guides/mpesa_business_playbook.pdf",
            downloadStatus = DownloadStatus.NOT_DOWNLOADED
        ),
        BusinessDocument(
            id = "doc_sci_plan_template",
            title = "SCI BASE Business Plan Format Specification",
            description = "Official JSON schema and printable structure for preparing business plan drafts compatible with SCI BUSINESS PLAN.",
            category = DocumentCategory.TEMPLATES,
            fileType = "PDF",
            sizeBytes = 1450000,
            version = "1.0",
            isPremium = false,
            remoteR2ObjectKey = "templates/sci_business_plan_specification.pdf",
            downloadStatus = DownloadStatus.NOT_DOWNLOADED
        )
    )

    val successStories: List<EntrepreneurProfile> = listOf(
        EntrepreneurProfile(
            id = "brian_mwangi",
            name = "Brian Mwangi",
            age = 24,
            county = "Nairobi (Kasarani)",
            ventureName = "Bamba Prints & Brand KE",
            category = "Digital Branding & Merch",
            startedWithKes = 3500,
            currentMonthlyRevenueKes = "KSh 180,000+",
            journeyStory = "After finishing college without a job offer, Brian started designing birthday flyers and church posters on his smartphone using free Canva. He saved KSh 20,000 within 6 months, leased a mini heat-press, and started branding corporate mugs, polo shirts, and tote bags for local SMEs.",
            keyChallenges = listOf(
                "Delayed payments from corporate clients after delivering bulk merchandise.",
                "Frequent daytime power blackouts halting print deadlines.",
                "Learning to separate business capital from personal living expenses."
            ),
            keyAdvice = "Usione aibu kuanza kidogo. Use the smartphone already in your hand to master one marketable skill. Kenyans will gladly pay anyone who shows up on time and delivers crisp quality.",
            primarySkillsUsed = listOf("Graphic Design", "Social Media", "Printing"),
            avatarInitials = "BM",
            mentorAvailable = true,
            mentorshipFocus = "Branding, SME Sales & Bootstrapping Tech",
            verificationStatus = ProfileVerificationStatus.ILLUSTRATIVE_CASE_STUDY,
            citationSource = "Field interviews with Kasarani youth graphic designers & KNBS Micro-Enterprise Survey 2024",
            lastVerifiedDate = "October 2026",
            mentorshipDisclaimer = "Mentorship requests are routed to verified volunteer advisors from the YEDF and SCI BASE COLLECTIONS network."
        ),
        EntrepreneurProfile(
            id = "faith_achieng",
            name = "Faith Achieng",
            age = 26,
            county = "Kisumu (Dunga)",
            ventureName = "Kuku Smart Poultry Breeders",
            category = "Agribusiness & Hatchery",
            startedWithKes = 6500,
            currentMonthlyRevenueKes = "KSh 240,000+",
            journeyStory = "Faith converted an unused corner of her family compound into a brooder. Armed with KSh 6,500 from Hustler Fund and personal savings, she purchased 30 day-old improved Kienyeji chicks. She documented her feeding routines and biosecurity tips on TikTok. Today she runs an incubator hatching 1,500 chicks monthly.",
            keyChallenges = listOf(
                "Losing 15 chicks in her first batch due to improper brooding temperature.",
                "Skyrocketing commercial feed prices eating into margins.",
                "Skepticism from older farmers who doubted a young woman in poultry farming."
            ),
            keyAdvice = "Farming is not an old person's retirement plan—it is a modern data-driven science. If you vaccinate religiously and formulate part of your feed locally (like azolla and greens), your profit margins will surprise you.",
            primarySkillsUsed = listOf("Poultry Breeding", "Social Selling", "Farm Recordkeeping"),
            avatarInitials = "FA",
            mentorAvailable = true,
            mentorshipFocus = "Agribusiness, Feed Formulations & Hatchery",
            verificationStatus = ProfileVerificationStatus.ILLUSTRATIVE_CASE_STUDY,
            citationSource = "Western Kenya Youth Agri-preneurs Forum & Ministry of Agriculture Livestock Advisory",
            lastVerifiedDate = "October 2026",
            mentorshipDisclaimer = "Mentorship requests are routed to verified agricultural extension volunteers and peer mentors."
        ),
        EntrepreneurProfile(
            id = "kevin_otieno",
            name = "Kevin Otieno",
            age = 23,
            county = "Eldoret (West Indies)",
            ventureName = "EcoSpark Mobile Detailing",
            category = "Auto Care & Cleaning",
            startedWithKes = 12000,
            currentMonthlyRevenueKes = "KSh 140,000+",
            journeyStory = "Kevin noticed that busy professionals in Eldoret spent 2-3 hours waiting in queue at static car washes every Saturday. He pooled money with a friend to buy a portable battery-powered high pressure washer and started offering doorstep car wash and seat shampooing at residential compounds.",
            keyChallenges = listOf(
                "Hesitation by car owners to hand over keys to a mobile crew.",
                "Seasonal drop in demand during heavy rainy months.",
                "Managing water transportation without an expensive vehicle."
            ),
            keyAdvice = "Trust is the currency of youth entrepreneurship. Wear clean branded overalls, take photos before and after, and always return lost coins found under seats. Word-of-mouth recommendations will fill your calendar effortlessly.",
            primarySkillsUsed = listOf("Car Detailing", "Customer Relations", "Boda Logistics"),
            avatarInitials = "KO",
            mentorAvailable = true,
            mentorshipFocus = "Service Operations, Client Trust & Team Building",
            verificationStatus = ProfileVerificationStatus.ILLUSTRATIVE_CASE_STUDY,
            citationSource = "Eldoret Youth Boda and Mobile Cleaning Enterprise Syndicate Study",
            lastVerifiedDate = "October 2026",
            mentorshipDisclaimer = "Mentorship requests are matched with experienced service business founders."
        )
    )

    val marketSectorTrends: List<MarketSectorTrend> = listOf(
        MarketSectorTrend(
            id = "climate_agri",
            sectorName = "Climate-Smart Agribusiness & Urban Micro-Farms",
            growthTag = "+28% YoY Demand",
            summary = "Urban migration in Kenya has sparked an unprecedented appetite for fresh, traceable organic vegetables, improved indigenous poultry, and greenhouse produce.",
            whyPromisingForYouth = "Bypasses traditional land ownership barriers through vertical bag farming, hydroponics, and peri-urban leased plots. Young farmers leverage TikTok & WhatsApp to sell directly to consumers at retail prices, cutting out ruthless brokers.",
            potentialCustomerBase = listOf(
                "Health-conscious urban households in Nairobi, Kisumu, Nakuru & Mombasa",
                "Estate greengrocers (Mama Mbogas) looking for consistent supply",
                "Restaurants, local cafeterias, and gym enthusiasts seeking lean poultry"
            ),
            averageStartupCapitalRange = "KSh 8,000 - 35,000",
            hotNiches = listOf("Improved Kienyeji egg production", "Black soldier fly (BSF) insect protein", "Oyster mushroom cultivation", "Kitchen garden multi-storey bags"),
            keyEnablersInKenya = listOf("Hustler Fund group agriculture loans", "Free extension training from KALRO", "Mobile soil testing kits"),
            evidenceType = TrendEvidenceType.QUALITATIVE_DEMAND,
            sourceCitation = "Ministry of Agriculture Kenya Youth Agribusiness Strategy & KALRO Extension Bulletins",
            publicationDate = "2024",
            geographicScope = "National with concentration in Nairobi peri-urban, Kiambu, Nakuru, and Uasin Gishu",
            periodCovered = "2023 - 2025"
        ),
        MarketSectorTrend(
            id = "hyperlocal_logistics",
            sectorName = "Hyperlocal Boda Delivery & Estate Dark Stores",
            growthTag = "+34% Surge",
            summary = "E-commerce in Kenya is booming beyond city centres into residential estates like Ruiru, Kitengela, Syokimau, Ngong, and Eldoret where customers expect 45-minute delivery.",
            whyPromisingForYouth = "Youth who already possess motorbikes or bicycles can build high-retention client bases by partnering with local pharmacies, online thrift stores, bakeries, and butcheries.",
            potentialCustomerBase = listOf(
                "Instagram & TikTok online vendors needing dependable couriers",
                "Neighbourhood butcheries and fresh produce vendors",
                "Working parents ordering emergency groceries and baby diapers"
            ),
            averageStartupCapitalRange = "KSh 6,000 - 20,000",
            hotNiches = listOf("Cold-chain cake & food dispatch", "Same-day estate parcel relay", "Late night medicine & essentials run"),
            keyEnablersInKenya = listOf("Pochi La Biashara cashless payments", "Google Maps offline pins", "Electric Boda leasing options (e.g. Roam, Ampersand)"),
            evidenceType = TrendEvidenceType.QUALITATIVE_DEMAND,
            sourceCitation = "Communications Authority of Kenya (CA) Postal and Courier Sub-Sector Quarterly Reports",
            publicationDate = "2024",
            geographicScope = "Major urban counties (Nairobi, Mombasa, Nakuru, Kisumu)",
            periodCovered = "2022 - 2025"
        ),
        MarketSectorTrend(
            id = "digital_sme_branding",
            sectorName = "Social Commerce Content & Micro-SaaS for SMEs",
            growthTag = "+42% Growth",
            summary = "Over 1.5 million Kenyan micro and small enterprises (MSMEs) now realize they must advertise on TikTok, Instagram, and WhatsApp but lack the technical know-how or time.",
            whyPromisingForYouth = "Zero inventory overhead and near-instant cash flow. If you can shoot short videos with a smartphone and edit on CapCut, local hardware shops, salons, and private clinics will pay you on monthly retainers.",
            potentialCustomerBase = listOf(
                "Boutiques, salons and barbershops in estate shopping malls",
                "Private nursery and primary schools seeking enrollment growth",
                "Auto garages, spare parts dealers, and real estate agents"
            ),
            averageStartupCapitalRange = "KSh 2,500 - 15,000",
            hotNiches = listOf("TikTok product showcase reels", "Canva catalogue & price list redesign", "WhatsApp Business catalog automation"),
            keyEnablersInKenya = listOf("Affordable Safaricom 5G/4G home bundles", "Canva & CapCut mobile AI tools", "High smartphone penetration among youth"),
            evidenceType = TrendEvidenceType.QUALITATIVE_DEMAND,
            sourceCitation = "KNBS Micro, Small and Medium Enterprises (MSME) Basic Report",
            publicationDate = "2024",
            geographicScope = "Urban and peri-urban Kenya",
            periodCovered = "2023 - 2025"
        )
    )

    val youthFundGuides: List<YouthFundGuide> = listOf(
        YouthFundGuide(
            name = "Hustler Fund (Individual & Group)",
            provider = "Government of Kenya (State Dept for MSME)",
            maxAmountKes = "KSh 500 up to KSh 50,000 (Individual) / KSh 250,000 (Group)",
            interestRate = "8% per annum (pro-rated daily)",
            targetGroup = "All Kenyan citizens aged 18+ with active phone number",
            eligibilityChecklist = listOf(
                "Valid Kenyan National ID",
                "Active Safaricom, Airtel, or Telkom line registered for at least 90 days",
                "Good repayment score on previous micro-tranches"
            ),
            applicationProcess = "Dial USSD code *254#, accept terms, enter M-PESA PIN. Funds disburse instantly into your mobile wallet without paper collateral.",
            ussdOrPortal = "*254#",
            officialSourceUrl = "https://www.financialinclusion.go.ke",
            lastVerifiedDate = "October 2026",
            approvalDisclaimer = "Eligibility limit increases with punctual repayments. Requirements determined by mobile credit scoring algorithm."
        ),
        YouthFundGuide(
            name = "Youth Enterprise Development Fund (YEDF)",
            provider = "Ministry of Youth Affairs & The Arts",
            maxAmountKes = "KSh 50,000 - KSh 1,000,000",
            interestRate = "0% interest (6% one-off administrative fee)",
            targetGroup = "Kenyan youth aged 18 to 35 years (Individuals & registered groups)",
            eligibilityChecklist = listOf(
                "Registered business entity on eCitizen or registered youth group (Chama)",
                "All members aged 18-35 years",
                "County youth officer endorsement and business proposal"
            ),
            applicationProcess = "Download application form from youthfund.go.ke or visit your Sub-County Youth Officer at the Sub-County headquarters.",
            ussdOrPortal = "www.youthfund.go.ke",
            officialSourceUrl = "https://www.youthfund.go.ke",
            lastVerifiedDate = "October 2026",
            approvalDisclaimer = "Funds disbursed per county allocation quotas upon review by sub-county committees."
        ),
        YouthFundGuide(
            name = "Uwezo Fund",
            provider = "Constituency Level Fund",
            maxAmountKes = "KSh 50,000 - KSh 500,000",
            interestRate = "0% interest (1% one-off admin fee)",
            targetGroup = "Youth, women and persons with disabilities in organized groups",
            eligibilityChecklist = listOf(
                "Group registered with Department of Social Services for at least 6 months",
                "Table banking or Chama account record",
                "Based in the specific constituency where applying"
            ),
            applicationProcess = "Submit group proposal to the Uwezo Fund Constituency Committee via the local Member of Parliament (MP) office.",
            ussdOrPortal = "www.uwezo.go.ke",
            officialSourceUrl = "https://www.uwezo.go.ke",
            lastVerifiedDate = "October 2026",
            approvalDisclaimer = "Vetted and awarded by constituency committees during designated disbursement cycles."
        )
    )

    // Recommendation Engine integration
    fun matchBusinessIdeas(query: UserProfileQuery): List<BusinessIdea> {
        val evaluated = businessIdeas.map { idea ->
            RecommendationEngine.evaluate(idea, query, defaultSkills)
        }
        return evaluated.sortedByDescending { it.matchScore }
    }

    // Legacy overload for backward compatibility with existing tests
    fun matchBusinessIdeas(
        userSkillIds: Set<String>,
        selectedInterests: Set<InterestArea>,
        capitalTier: CapitalTier? = null,
        environment: EnvironmentType? = null
    ): List<BusinessIdea> {
        val query = UserProfileQuery(
            skillIds = userSkillIds,
            interestAreas = selectedInterests,
            capitalTier = capitalTier,
            environment = environment
        )
        return matchBusinessIdeas(query).take(10).ifEmpty { businessIdeas.take(6) }
    }

    // Persistence: Saved Ideas
    fun getSavedIdeaIds(): Set<String> {
        return prefs.getStringSet("saved_ideas", emptySet()) ?: emptySet()
    }

    fun toggleSaveIdea(ideaId: String): Boolean {
        val current = getSavedIdeaIds().toMutableSet()
        val newState = if (current.contains(ideaId)) {
            current.remove(ideaId)
            false
        } else {
            current.add(ideaId)
            true
        }
        prefs.edit().putStringSet("saved_ideas", current).apply()
        return newState
    }

    // Persistence: Compare Ideas
    fun getComparedIdeaIds(): Set<String> {
        return prefs.getStringSet("compared_ideas", emptySet()) ?: emptySet()
    }

    fun toggleCompareIdea(ideaId: String): Boolean {
        val current = getComparedIdeaIds().toMutableSet()
        val newState = if (current.contains(ideaId)) {
            current.remove(ideaId)
            false
        } else {
            if (current.size >= 3) current.remove(current.first()) // max 3 to compare
            current.add(ideaId)
            true
        }
        prefs.edit().putStringSet("compared_ideas", current).apply()
        return newState
    }

    // Persistence: Journey Progress per Idea
    fun getJourneyProgress(ideaId: String): HustleJourneyProgress {
        val raw = prefs.getString("journey_progress_$ideaId", null) ?: return HustleJourneyProgress(ideaId)
        return try {
            val obj = JSONObject(raw)
            val completedArray = obj.optJSONArray("completed_tasks") ?: JSONArray()
            val completedSet = mutableSetOf<String>()
            for (i in 0 until completedArray.length()) {
                completedSet.add(completedArray.getString(i))
            }
            HustleJourneyProgress(
                ideaId = ideaId,
                completedTaskIds = completedSet,
                notes = obj.optString("notes", ""),
                targetCapitalKes = obj.optInt("target_capital", 0),
                savedCapitalKes = obj.optInt("saved_capital", 0),
                lastUpdated = obj.optLong("last_updated", System.currentTimeMillis())
            )
        } catch (e: Exception) {
            HustleJourneyProgress(ideaId)
        }
    }

    fun saveJourneyProgress(progress: HustleJourneyProgress) {
        val obj = JSONObject().apply {
            put("idea_id", progress.ideaId)
            val arr = JSONArray()
            progress.completedTaskIds.forEach { arr.put(it) }
            put("completed_tasks", arr)
            put("notes", progress.notes)
            put("target_capital", progress.targetCapitalKes)
            put("saved_capital", progress.savedCapitalKes)
            put("last_updated", System.currentTimeMillis())
        }
        prefs.edit().putString("journey_progress_${progress.ideaId}", obj.toString()).apply()
    }

    // Persistence: SCI Business Plan Drafts
    fun getBusinessPlanDraft(ideaId: String): SciBusinessPlanDraft {
        val raw = prefs.getString("sci_plan_$ideaId", null)
        if (raw != null) {
            try {
                val obj = JSONObject(raw)
                return SciBusinessPlanDraft(
                    planId = obj.optString("plan_id", "sci_plan_$ideaId"),
                    sourceIdeaId = ideaId,
                    businessName = obj.optString("business_name", ""),
                    problem = obj.optString("problem", ""),
                    solution = obj.optString("solution", ""),
                    targetMarket = obj.optString("target_market", ""),
                    competitors = obj.optString("competitors", ""),
                    productsAndServices = obj.optString("products_and_services", ""),
                    pricingStrategy = obj.optString("pricing_strategy", ""),
                    marketingPlan = obj.optString("marketing_plan", ""),
                    operationsPlan = obj.optString("operations_plan", ""),
                    startupCostsKes = obj.optInt("startup_costs", 10000),
                    monthlyRevenueKes = obj.optInt("monthly_revenue", 40000),
                    monthlyExpensesKes = obj.optInt("monthly_expenses", 20000),
                    keyRisks = obj.optString("key_risks", ""),
                    growthStrategy = obj.optString("growth_strategy", ""),
                    currency = obj.optString("currency", "KES"),
                    createdTimestamp = obj.optLong("created_timestamp", System.currentTimeMillis()),
                    lastModifiedTimestamp = obj.optLong("last_modified_timestamp", System.currentTimeMillis())
                )
            } catch (e: Exception) {
                // fallback to generating from idea
            }
        }
        val idea = businessIdeas.find { it.id == ideaId } ?: businessIdeas.first()
        return SciBusinessPlanDraft.fromIdea(idea)
    }

    fun saveBusinessPlanDraft(draft: SciBusinessPlanDraft) {
        prefs.edit().putString("sci_plan_${draft.sourceIdeaId}", draft.toJsonString()).apply()
    }

    // Persistence: Document Library
    fun getDocumentLibrary(): List<BusinessDocument> {
        val downloadedSet = prefs.getStringSet("downloaded_docs", emptySet()) ?: emptySet()
        return defaultDocuments.map { doc ->
            if (downloadedSet.contains(doc.id)) {
                doc.copy(
                    downloadStatus = DownloadStatus.DOWNLOADED,
                    localFilePath = "/data/user/0/com.aistudio.biasharahub.kenyau/files/docs/${doc.id}.pdf"
                )
            } else {
                doc
            }
        }
    }

    fun toggleDocumentDownload(docId: String): Boolean {
        val set = (prefs.getStringSet("downloaded_docs", emptySet()) ?: emptySet()).toMutableSet()
        val isNowDownloaded = if (set.contains(docId)) {
            set.remove(docId)
            false
        } else {
            set.add(docId)
            true
        }
        prefs.edit().putStringSet("downloaded_docs", set).apply()
        return isNowDownloaded
    }

    // Persistence: Mentorship Requests
    fun saveMentorshipRequest(request: MentorshipRequest) {
        val existingJson = prefs.getString("mentorship_requests", "[]") ?: "[]"
        val array = JSONArray(existingJson)
        val obj = JSONObject().apply {
            put("id", request.id)
            put("mentorName", request.mentorName)
            put("applicantName", request.applicantName)
            put("phoneOrEmail", request.phoneOrEmail)
            put("selectedSkillOrVenture", request.selectedSkillOrVenture)
            put("questionOrGoal", request.questionOrGoal)
            put("preferredMode", request.preferredMode)
            put("timestamp", request.timestamp)
            put("status", request.status)
        }
        array.put(obj)
        prefs.edit().putString("mentorship_requests", array.toString()).apply()
    }

    fun getMentorshipRequests(): List<MentorshipRequest> {
        val existingJson = prefs.getString("mentorship_requests", "[]") ?: "[]"
        val array = JSONArray(existingJson)
        val list = mutableListOf<MentorshipRequest>()
        for (i in 0 until array.length()) {
            val obj = array.getJSONObject(i)
            list.add(
                MentorshipRequest(
                    id = obj.getString("id"),
                    mentorName = obj.getString("mentorName"),
                    applicantName = obj.getString("applicantName"),
                    phoneOrEmail = obj.getString("phoneOrEmail"),
                    selectedSkillOrVenture = obj.getString("selectedSkillOrVenture"),
                    questionOrGoal = obj.getString("questionOrGoal"),
                    preferredMode = obj.getString("preferredMode"),
                    timestamp = obj.getLong("timestamp"),
                    status = obj.optString("status", "Pending Review")
                )
            )
        }
        return list
    }

    // Data wipe / privacy clear
    fun clearUserData() {
        prefs.edit().clear().apply()
    }
}
