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
            id = "urban_farming",
            title = "Urban Vegetable Farming",
            shengTitle = "Kilimo cha Mboga Mjini",
            category = SkillCategory.AGRIBUSINESS,
            interestArea = InterestArea.FARMING_LIVESTOCK,
            summary = "Urban vegetable farming is a profitable business that can be started in small spaces using modern techniques like vertical farming, container gardening, or greenhouse systems.",
            problemBeingSolved = "City residents struggle with high food prices and chemical-laden vegetables sold in distant wholesale markets.",
            proposedSolution = "Produce fresh, chemical-free sukuma wiki, spinach, capsicum, and herbs using space-efficient vertical containers and hydroponic trays.",
            targetCustomers = listOf("Health-conscious estate residents", "Local estate mama mbogas and grocery stalls", "Neighbourhood restaurants and food joints"),
            matchedSkills = listOf("horticulture", "poultry"),
            capitalMinKes = 10000,
            capitalMaxKes = 50000,
            capitalTier = CapitalTier.LOW,
            capitalBreakdown = mapOf(
                "Vertical containers & coco peat seedling trays" to 8000,
                "Certified high-yield vegetable seeds & organic manure" to 4500,
                "Drip irrigation tubing & spray nozzles" to 5500,
                "Balcony shade netting & pest control neem oil" to 3000
            ),
            estimatedDailyRevenueKes = 800..2500,
            estimatedMonthlyProfitKes = 15000..60000,
            paybackPeriod = "1.5 - 2 months",
            idealLocations = listOf(EnvironmentType.URBAN, EnvironmentType.PERI_URBAN),
            businessModel = BusinessModelType.HYBRID,
            riskLevel = RiskLevel.LOW,
            timeCommitment = TimeCommitment.PART_TIME,
            requiredEquipmentIds = listOf("land_space", "hand_tools"),
            equipmentNeeded = listOf("Vertical planting towers or wooden crates", "Drip irrigation pipe kit", "Water storage bucket", "Garden shears & hand trowel"),
            supplierAndSourcingGuidance = "Purchase quality hybrid seeds from Kenya Seed Company or Simlaw Seeds along Kijabe Street; buy coco peat from peri-urban nursery centers.",
            pricingConsiderations = "Harvest bunches sell for KSh 30 - 50 each; weekly household organic vegetable baskets retail at KSh 500 - 800.",
            marketingStrategy = "Share daily growth photos on estate WhatsApp groups and offer free sample bunches to 5 influential neighbours.",
            competitorResearchGuidance = "Supermarket organic vegetables are priced double; your freshly cut greens delivered warm will win instantly.",
            launchSteps = listOf(
                "Source quality hybrid seeds and seedling trays.",
                "Prepare vertical wooden shelves or balcony planter boxes.",
                "Install drip lines and test automated watering.",
                "Launch first harvest pre-orders on estate WhatsApp group."
            ),
            compliance = listOf(
                ComplianceRequirement("Local Agricultural Officer Advisory", "Ministry of Agriculture", "Free", "Free biosecurity and seedling health guidance", false),
                ComplianceRequirement("County Urban Agriculture Guideline", "County Environment Dept", "Free", "Compliance with residential water usage policies", false)
            ),
            mpesaTip = "Collect weekly advance M-PESA subscriptions for basket deliveries on Monday mornings.",
            insiderTips = listOf(
                "Spinach and coriander (dhania) have the fastest turnaround: you can harvest every 2-3 weeks continuously.",
                "Vertical towers let you grow 80 plants in just 1 square meter of balcony space."
            ),
            risksAndMitigations = listOf(
                "Pest infestation" to "Spray natural organic neem oil extract once every week instead of chemical pesticides.",
                "Water supply interruption" to "Connect a 100L elevated rainwater or tap storage container."
            ),
            lastReviewedDate = "October 2026",
            rating = 4.5,
            reviewCount = 120,
            thumbnailType = "farming"
        ),

        BusinessIdea(
            id = "phone_accessories",
            title = "Mobile Phone Accessories Shop",
            shengTitle = "Duka la Vifaa vya Simu",
            category = SkillCategory.RETAIL_COMMERCE,
            interestArea = InterestArea.ONLINE_FREELANCING,
            summary = "High-turnover retail shop stocking tempered glass screen protectors, fast chargers, power banks, earphones, phone covers, and smart accessories.",
            problemBeingSolved = "Smartphone users frequently break screen protectors, lose charging cables, and need quick reliable replacements near transit stops.",
            proposedSolution = "Quick-turnaround display booth or estate shop offering instant screen protector installation, cable testing, and genuine mobile accessories.",
            targetCustomers = listOf("Commuters at matatu stops", "Estate residents", "Bodaboda riders needing durable phone holders"),
            matchedSkills = listOf("phone_repair", "retail_commerce", "social_media"),
            capitalMinKes = 50000,
            capitalMaxKes = 200000,
            capitalTier = CapitalTier.MEDIUM,
            capitalBreakdown = mapOf(
                "Wholesale stock (chargers, screen protectors, cases)" to 45000,
                "Display glass counter & LED lighting" to 18000,
                "Shop deposit & branding sign" to 25000,
                "Working capital & spare parts" to 12000
            ),
            estimatedDailyRevenueKes = 3000..8500,
            estimatedMonthlyProfitKes = 35000..90000,
            paybackPeriod = "2 - 3 months",
            idealLocations = listOf(EnvironmentType.URBAN, EnvironmentType.PERI_URBAN),
            businessModel = BusinessModelType.OFFLINE,
            riskLevel = RiskLevel.MEDIUM,
            timeCommitment = TimeCommitment.FULL_TIME,
            requiredEquipmentIds = listOf("hand_tools"),
            equipmentNeeded = listOf("Glass display counter", "Hot air gun & screen cleaning cloths", "Cable test device", "Laminated price list"),
            supplierAndSourcingGuidance = "Wholesale markets in Nairobi CBD (Luthuli Avenue, Munyu Road, Dubois Road) at 50-70% lower than retail.",
            pricingConsiderations = "Ceramic screen protectors wholesale for KSh 40 and install for KSh 200 (400% margin!). Fast chargers wholesale at KSh 180 and retail at KSh 500.",
            marketingStrategy = "Offer free screen cleaning and test every cable in front of the buyer to build unwavering trust.",
            competitorResearchGuidance = "Avoid unbranded fake chargers that explode; stock reliable certified brands like Oraimo, Baseus, and generic Tier-1 equivalents.",
            launchSteps = listOf(
                "Secure a high-footfall corner stall near a busy estate stage.",
                "Purchase initial fast-moving inventory from Luthuli Avenue wholesalers.",
                "Mount bright LED lighting and an eye-catching neon shop sign.",
                "Offer free installation on all screen protectors to build initial clientele."
            ),
            compliance = listOf(
                ComplianceRequirement("Single Business Permit", "County Government", "KSh 4,500 - 8,500", "Annual commercial retail trading license", true),
                ComplianceRequirement("Business Name Registration", "eCitizen BRS", "KSh 950", "Official business registration", false)
            ),
            mpesaTip = "Have your Pochi La Biashara QR standee front and center on the counter for instant tap payment.",
            insiderTips = listOf(
                "Type-C cables, ceramic matte screen protectors, and 10,000mAh power banks are your fastest money-makers.",
                "Upsell a phone case whenever a customer buys a screen protector for an immediate KSh 300 boost."
            ),
            risksAndMitigations = listOf(
                "Slow-moving stock" to "Only purchase models for the top 10 phones in Kenya (Infinix, Tecno, Samsung A-series, Redmi).",
                "Counterfeits" to "Test every wholesale batch before leaving Luthuli Avenue."
            ),
            lastReviewedDate = "October 2026",
            rating = 4.2,
            reviewCount = 98,
            thumbnailType = "electronics"
        ),

        BusinessIdea(
            id = "digital_marketing_agency",
            title = "Digital Marketing Services",
            shengTitle = "Huduma za Digital Marketing",
            category = SkillCategory.SERVICES_LOGISTICS,
            interestArea = InterestArea.ONLINE_FREELANCING,
            summary = "Help local SMEs, clinics, schools, and salons grow their revenue through Google Business optimization, TikTok campaigns, Meta ads, and social content.",
            problemBeingSolved = "Small business owners struggle to reach new customers online and waste money on ineffective boosted posts.",
            proposedSolution = "Performance-driven monthly marketing packages including video reels, local SEO, WhatsApp automation, and targeted ad campaigns.",
            targetCustomers = listOf("Private dental clinics & pharmacies", "Beauty salons & spas", "Real estate agents", "Local hardware stores"),
            matchedSkills = listOf("graphic_design", "social_media", "photo_video", "web_dev"),
            capitalMinKes = 10000,
            capitalMaxKes = 100000,
            capitalTier = CapitalTier.LOW,
            capitalBreakdown = mapOf(
                "Canva Pro & CapCut subscription" to 3000,
                "High-speed 4G data & Wi-Fi router" to 4000,
                "Portfolio website hosting & domain" to 3000,
                "Client acquisition ad budget" to 5000
            ),
            estimatedDailyRevenueKes = 2000..6000,
            estimatedMonthlyProfitKes = 30000..95000,
            paybackPeriod = "3 - 4 weeks",
            idealLocations = listOf(EnvironmentType.ONLINE, EnvironmentType.URBAN, EnvironmentType.PERI_URBAN),
            businessModel = BusinessModelType.ONLINE,
            riskLevel = RiskLevel.LOW,
            timeCommitment = TimeCommitment.FLEXIBLE,
            requiredEquipmentIds = listOf("smartphone"),
            equipmentNeeded = listOf("Laptop or high-end smartphone", "Ring light & tripod for video shoots", "Stable internet connection"),
            supplierAndSourcingGuidance = "SaaS software online via debit/prepaid cards or M-PESA Global Pay virtual cards.",
            pricingConsiderations = "Charge KSh 12,000 - 25,000 monthly retainer per client. 4 active clients yield KSh 60,000+ steady profit.",
            marketingStrategy = "Walk into businesses with poor Google Maps listings or dormant social pages, optimize their profile for free, then pitch ongoing management.",
            competitorResearchGuidance = "Large marketing agencies charge KSh 100k+ and ignore small businesses; deliver quick, tangible local leads to stand out.",
            launchSteps = listOf(
                "Create 3 case studies optimizing profiles of local business acquaintances.",
                "Pitch 10 local service businesses offering a 14-day lead generation trial.",
                "Deliver real customer calls and WhatsApp inquiries.",
                "Close 3 ongoing monthly retainer contracts."
            ),
            compliance = listOf(
                ComplianceRequirement("Individual KRA PIN", "KRA iTax", "Free", "Tax compliance for corporate payments", false),
                ComplianceRequirement("Business Name Registration", "eCitizen BRS", "KSh 950", "Official registered trading name", false)
            ),
            mpesaTip = "Invoice clients via professional PDF quotes with embedded Paybill / Till payment terms.",
            insiderTips = listOf(
                "Focus on short-form vertical video (TikTok & Reels); businesses will pay premium rates for video views that convert into customers.",
                "Google Business Profile optimization drives immediate phone calls without ad spend."
            ),
            risksAndMitigations = listOf(
                "Client churn" to "Send a simple monthly 1-page report detailing phone calls and inquiries generated.",
                "Late payment" to "Charge 100% upfront at the start of each service month."
            ),
            lastReviewedDate = "October 2026",
            rating = 4.4,
            reviewCount = 76,
            thumbnailType = "tech"
        ),

        BusinessIdea(
            id = "clean_energy_solutions",
            title = "Clean Energy Solutions",
            shengTitle = "Mwangaza wa Solar & Biogas",
            category = SkillCategory.DIGITAL_TECH,
            interestArea = InterestArea.CRAFTS_FABRICATION,
            summary = "Sales, installation, and maintenance of affordable solar backup systems, solar water heaters, and rural micro-solar solutions for homes and farms.",
            problemBeingSolved = "High KPLC electricity tariffs and frequent grid blackouts disrupt businesses and households across Kenyan towns and counties.",
            proposedSolution = "Turnkey solar installations providing reliable clean backup power, solar water pumping for farms, and energy-efficient LED upgrades.",
            targetCustomers = listOf("Off-grid and peri-urban homeowners", "Agri-business farms needing water pumping", "Estate barbershops and salons avoiding blackouts"),
            matchedSkills = listOf("plumbing_elec", "welding", "carpentry"),
            capitalMinKes = 100000,
            capitalMaxKes = 350000,
            capitalTier = CapitalTier.EXPANDED,
            capitalBreakdown = mapOf(
                "Sample demonstration solar inverter & lithium battery kit" to 65000,
                "Electrical multimeter, crimping tools & ladders" to 18000,
                "EPRA technician certification & license" to 12000,
                "Marketing flyers & local demo booth" to 8000
            ),
            estimatedDailyRevenueKes = 4000..12000,
            estimatedMonthlyProfitKes = 60000..180000,
            paybackPeriod = "2 - 3 months",
            idealLocations = listOf(EnvironmentType.PERI_URBAN, EnvironmentType.RURAL, EnvironmentType.URBAN),
            businessModel = BusinessModelType.HYBRID,
            riskLevel = RiskLevel.MEDIUM,
            timeCommitment = TimeCommitment.FULL_TIME,
            requiredEquipmentIds = listOf("hand_tools"),
            equipmentNeeded = listOf("Digital multimeter & clamp meter", "Heavy-duty wire strippers & crimpers", "Safety harness & folding ladder", "Drill and mounting brackets"),
            supplierAndSourcingGuidance = "Wholesale solar importers along Commercial Street and Dar es Salaam Road in Nairobi Industrial Area.",
            pricingConsiderations = "Hardware markup is 20-25%; professional labor installation fees are KSh 15,000 - 35,000 per residential installation.",
            marketingStrategy = "Set up live solar battery demonstrations outside town hardware stores during Saturday market days.",
            competitorResearchGuidance = "Many solar installers offer poor after-sales support; guarantee free 6-month quarterly checkups to gain referrals.",
            launchSteps = listOf(
                "Complete EPRA Solar PV Class T2/T3 certification.",
                "Partner with 2 wholesale hardware distributors in town.",
                "Install a pilot demonstration system in a visible commercial premises.",
                "Offer turnkey quote financing options through SACCOs and chamas."
            ),
            compliance = listOf(
                ComplianceRequirement("EPRA Solar PV License", "EPRA Kenya", "KSh 5,000 - 10,000", "Mandatory statutory license for grid/off-grid solar installation", true),
                ComplianceRequirement("County Single Business Permit", "County Revenue", "KSh 5,000 - 9,000", "Trade permit for technical contractor operations", true)
            ),
            mpesaTip = "Accept 60% equipment deposit prior to purchasing materials, with 40% balance upon successful commissioning.",
            insiderTips = listOf(
                "Solar water pumping for poultry and vegetable farms is currently booming; farmers recover installation costs in 6 months compared to petrol pumps.",
                "Lithium LiFePO4 batteries have replaced lead-acid and command much higher customer satisfaction."
            ),
            risksAndMitigations = listOf(
                "Roof damage liability" to "Always inspect roofing sheets with the homeowner before mounting brackets.",
                "Equipment warranty claims" to "Only source Tier-1 solar panels with 10-year manufacturer warranties."
            ),
            lastReviewedDate = "October 2026",
            rating = 4.6,
            reviewCount = 54,
            thumbnailType = "solar"
        ),

        BusinessIdea(
            id = "fast_food_takeaway",
            title = "Fast Food Takeaway",
            shengTitle = "Hot Food & Fries Express",
            category = SkillCategory.FOOD_HOSPITALITY,
            interestArea = InterestArea.FOOD_BEVERAGE,
            summary = "Quick-service takeaway offering crispy fries, seasoned fried chicken, burgers, bhajia, and refreshing juices at busy footfall corridors.",
            problemBeingSolved = "Commuters and young workers need hot, tasty, affordable meals ready in under 3 minutes without sit-down restaurant delays.",
            proposedSolution = "Hygienic open-kitchen counter preparing golden crispy potato fries, spicy bhajias, and tender chicken wings with signature sauces.",
            targetCustomers = listOf("College students", "Boda riders & matatu crews", "Office workers on lunch breaks", "Evening estate commuters"),
            matchedSkills = listOf("cooking_baking", "beverages"),
            capitalMinKes = 15000,
            capitalMaxKes = 60000,
            capitalTier = CapitalTier.LOW,
            capitalBreakdown = mapOf(
                "Commercial deep fryer or stainless steel pan" to 12000,
                "Fresh potato sack, cooking oil & chicken cuts" to 9000,
                "Branded greaseproof packaging boxes" to 4000,
                "County food hygiene cert & branding" to 5000
            ),
            estimatedDailyRevenueKes = 2500..7000,
            estimatedMonthlyProfitKes = 25000..75000,
            paybackPeriod = "3 - 5 weeks",
            idealLocations = listOf(EnvironmentType.URBAN, EnvironmentType.PERI_URBAN),
            businessModel = BusinessModelType.OFFLINE,
            riskLevel = RiskLevel.LOW,
            timeCommitment = TimeCommitment.FULL_TIME,
            requiredEquipmentIds = listOf("kitchen"),
            equipmentNeeded = listOf("Thermostatic deep fryer or gas cooker", "Potato chip cutter", "Stainless steel display food warmer", "Serving tongs & paper liners"),
            supplierAndSourcingGuidance = "Potatoes directly from Kinangop or Marikiti wholesale market; bulk 20L cooking oil from edible oil wholesalers.",
            pricingConsiderations = "Chips plate retails at KSh 100 - 150 (cost is KSh 45); 1/4 fried chicken combo at KSh 250 (cost is KSh 140). High volume creates handsome profit.",
            marketingStrategy = "Free tasting toothpicks at lunchtime and mouthwatering sizzling sounds right at the storefront.",
            competitorResearchGuidance = "Ensure your cooking oil is always fresh and clean; dark burnt oil ruins taste and repels return customers.",
            launchSteps = listOf(
                "Secure a high-visibility kiosk or veranda counter near bus stops.",
                "Obtain County food handling certification.",
                "Procure potato cutter, fryer, and fresh bulk ingredients.",
                "Host an opening day half-price combo promotion."
            ),
            compliance = listOf(
                ComplianceRequirement("Food Handlers Medical Certificate", "County Health", "KSh 1,000", "Annual medical checkup for food workers", true),
                ComplianceRequirement("Single Business Permit", "County Revenue", "KSh 4,000 - 7,000", "Food & beverage kiosk license", true)
            ),
            mpesaTip = "Place a large laminated Pochi La Biashara number on the serving counter for rapid cashless payment.",
            insiderTips = listOf(
                "Double-frying your potatoes (blanching first at 160°C, then crisping at 190°C) produces world-class restaurant fries.",
                "Offer signature garlic-mayo and chili dip for KSh 20 extra—it is 90% profit!"
            ),
            risksAndMitigations = listOf(
                "Cooking oil price fluctuations" to "Buy 20L jerrycans in bulk when wholesale prices dip.",
                "Perishable stock waste" to "Prep potatoes daily based on measured peak lunch and evening demand."
            ),
            lastReviewedDate = "October 2026",
            rating = 4.3,
            reviewCount = 85,
            thumbnailType = "food"
        ),

        BusinessIdea(
            id = "bakery_business",
            title = "Bakery Business",
            shengTitle = "Maandazi, Cakes & Pastry Bake",
            category = SkillCategory.FOOD_HOSPITALITY,
            interestArea = InterestArea.FOOD_BEVERAGE,
            summary = "Artisanal bakery and snack enterprise delivering fluffy mandazis, meat pies, birthday cakes, banana loaves, and muffins to tea kiosks and estate homes.",
            problemBeingSolved = "Local tea kiosks and busy parents lack access to freshly baked, moist pastries that are free from artificial preservatives.",
            proposedSolution = "Consistent daily baking of warm breakfast pastries, wholesale mandazi supply to 10 local cafes, and customized weekend celebratory cakes.",
            targetCustomers = listOf("Local breakfast cafes & tea kiosks", "Estate families & schoolchildren", "Birthday & wedding event organizers"),
            matchedSkills = listOf("pastry_cakes", "cooking_baking"),
            capitalMinKes = 30000,
            capitalMaxKes = 120000,
            capitalTier = CapitalTier.MEDIUM,
            capitalBreakdown = mapOf(
                "Commercial 2-deck electric/gas baking oven" to 28000,
                "Planetary stand mixer & baking tins" to 14000,
                "Flour, sugar, yeast, butter, eggs bulk inventory" to 10000,
                "Display glass counter & packaging" to 8000
            ),
            estimatedDailyRevenueKes = 3000..9000,
            estimatedMonthlyProfitKes = 40000..110000,
            paybackPeriod = "2 - 3 months",
            idealLocations = listOf(EnvironmentType.PERI_URBAN, EnvironmentType.URBAN),
            businessModel = BusinessModelType.HYBRID,
            riskLevel = RiskLevel.MEDIUM,
            timeCommitment = TimeCommitment.FULL_TIME,
            requiredEquipmentIds = listOf("kitchen"),
            equipmentNeeded = listOf("Baking oven", "Heavy-duty electric mixer", "Assorted cake pans and muffin trays", "Cooling racks & pastry boxes"),
            supplierAndSourcingGuidance = "Source baking flour in 50kg sacks from Unga Limited or Pembe distributors; buy margarine in 10kg cartons.",
            pricingConsiderations = "Mandazis wholesale at KSh 8 and cafes sell at KSh 15. Custom 1kg birthday cakes cost KSh 600 to bake and sell at KSh 1,800 - 2,500.",
            marketingStrategy = "Share short time-lapse cake frosting videos on TikTok and Instagram; supply 5 local kiosks every morning at 5:30 AM sharp.",
            competitorResearchGuidance = "Kiosks switch bakery suppliers based on reliability; always deliver fresh warm pastries before the 6:00 AM breakfast rush.",
            launchSteps = listOf(
                "Acquire oven and trial recipe batches for signature mandazis and sponge cakes.",
                "Secure daily morning supply commitments with 5 neighbourhood tea joints.",
                "Design a clean digital birthday cake catalogue with photos and pricing.",
                "Deliver fresh batches daily and reinvest revenue into packaging."
            ),
            compliance = listOf(
                ComplianceRequirement("Food Handlers Certificate", "County Public Health", "KSh 1,000", "Food preparation statutory certificate", true),
                ComplianceRequirement("Food Hygiene Permit", "County Government", "KSh 3,500 - 6,000", "Sanitary approval for baking premises", true)
            ),
            mpesaTip = "Request 50% commitment deposit via M-PESA for custom cake orders.",
            insiderTips = listOf(
                "Early bird advantage: supply kiosks before 6:00 AM when tea crowds gather.",
                "Use real vanilla extract and fresh eggs; customers immediately taste the difference over commercial mass-produced bread."
            ),
            risksAndMitigations = listOf(
                "Flour spoilage" to "Store flour sacks on raised wooden pallets off the ground in dry ventilated conditions.",
                "Power cuts during baking" to "Use a hybrid gas-electric oven or maintain an LPG backup cylinder."
            ),
            lastReviewedDate = "October 2026",
            rating = 4.4,
            reviewCount = 110,
            thumbnailType = "bakery"
        ),

        BusinessIdea(
            id = "juice_smoothie_bar",
            title = "Juice and Smoothie Bar",
            shengTitle = "Fresh Fruit Blends & Smoothies",
            category = SkillCategory.FOOD_HOSPITALITY,
            interestArea = InterestArea.FOOD_BEVERAGE,
            summary = "Freshly extracted sugarcane, tropical fruit smoothies, cold-pressed detox juices, and fruit salad bowls served to health-conscious urban shoppers.",
            problemBeingSolved = "Urban workers drink sugary commercial sodas because pure, freshly squeezed fruit juices are scarce and overpriced.",
            proposedSolution = "Quick-blend health bar pressing cold sugarcane with ginger and lime, mango-passion blends, and avocado-banana smoothies on demand.",
            targetCustomers = listOf("Gym enthusiasts & athletes", "Office workers wanting healthy lunches", "Shoppers in busy malls and market avenues"),
            matchedSkills = listOf("beverages", "cooking_baking"),
            capitalMinKes = 12000,
            capitalMaxKes = 45000,
            capitalTier = CapitalTier.LOW,
            capitalBreakdown = mapOf(
                "Heavy-duty commercial blender (2200W)" to 8500,
                "Insulated ice chest & glass dispenser" to 4000,
                "Fresh wholesale fruit stock & bio cups" to 5500,
                "Branded pop-up stall sign" to 3000
            ),
            estimatedDailyRevenueKes = 2000..5500,
            estimatedMonthlyProfitKes = 20000..55000,
            paybackPeriod = "3 - 5 weeks",
            idealLocations = listOf(EnvironmentType.URBAN, EnvironmentType.PERI_URBAN),
            businessModel = BusinessModelType.OFFLINE,
            riskLevel = RiskLevel.LOW,
            timeCommitment = TimeCommitment.FULL_TIME,
            requiredEquipmentIds = listOf("kitchen"),
            equipmentNeeded = listOf("Heavy-duty high-wattage blender", "Sugarcane squeezer or cold press juicer", "Stainless steel counter & chopping board", "Takeaway cups & paper straws"),
            supplierAndSourcingGuidance = "Wholesale fruits from Marikiti (Wakulima) or Kangemi markets early morning at 40% lower costs than supermarkets.",
            pricingConsiderations = "Cost per 400ml cup is KSh 35 - 50. Sells briskly at KSh 100 - 150 (over 150% profit margin).",
            marketingStrategy = "Display vibrant whole pineapples, watermelons, and citrus on the counter; offer free 50ml shot samples to passersby.",
            competitorResearchGuidance = "Never add artificial water or refined sugar; 100% natural pure fruit flavor is your competitive moat.",
            launchSteps = listOf(
                "Source heavy-duty commercial blender from Luthuli Avenue.",
                "Set up supply relationships with reliable Wakulima fruit wholesalers.",
                "Position near a fitness center, hospital, or busy commercial walkway.",
                "Promote 5 signature blends (e.g. Detox Glow, Tropical Energy)."
            ),
            compliance = listOf(
                ComplianceRequirement("Food Handlers Health Cert", "County Health Dept", "KSh 1,000", "Annual medical screening", true),
                ComplianceRequirement("Vendor Permit", "County Government", "KSh 3,000", "Municipal trade authorization", true)
            ),
            mpesaTip = "Have your Pochi La Biashara code prominently displayed right at the blending counter.",
            insiderTips = listOf(
                "Sugarcane juice with ginger and lime is high-profit: 1 stalk of sugarcane (KSh 30) yields 3 cups selling for KSh 300 total!",
                "Freeze ripe bananas and mango chunks; blending them frozen gives a rich, creamy soft-serve ice cream texture without dairy."
            ),
            risksAndMitigations = listOf(
                "Fruit ripening and spoilage" to "Chop and freeze ripe fruits in portion ziplock bags immediately.",
                "Power cuts" to "Maintain a 12V battery inverter to power the blender during blackouts."
            ),
            lastReviewedDate = "October 2026",
            rating = 4.2,
            reviewCount = 76,
            thumbnailType = "juice"
        ),

        BusinessIdea(
            id = "catering_services",
            title = "Catering Services",
            shengTitle = "Events & Wedding Catering",
            category = SkillCategory.FOOD_HOSPITALITY,
            interestArea = InterestArea.FOOD_BEVERAGE,
            summary = "On-demand catering for corporate boardroom lunches, weddings, dowry ruracios, church fellowships, and birthday parties with authentic Kenyan delicacies.",
            problemBeingSolved = "Event hosts are overwhelmed preparing food for 50-200 guests and want professional, reliable, delicious culinary execution without stress.",
            proposedSolution = "Full-service catering team preparing pilau, chapati, mukimo, goat stew, grilled meats, and crisp salads delivered warm with chafing dish setups.",
            targetCustomers = listOf("Corporate HR departments for team retreats", "Wedding & ruracio families", "Chama anniversary celebrations"),
            matchedSkills = listOf("cooking_baking", "events_decor"),
            capitalMinKes = 40000,
            capitalMaxKes = 150000,
            capitalTier = CapitalTier.MEDIUM,
            capitalBreakdown = mapOf(
                "Stainless steel chafing dishes set (6 units)" to 24000,
                "Large cooking sufurias & catering utensils" to 15000,
                "Uniform aprons, chef hats & serving gloves" to 6000,
                "Portfolio food tasting photo session & flyers" to 7000
            ),
            estimatedDailyRevenueKes = 4000..15000,
            estimatedMonthlyProfitKes = 50000..140000,
            paybackPeriod = "1.5 - 2 months",
            idealLocations = listOf(EnvironmentType.URBAN, EnvironmentType.PERI_URBAN),
            businessModel = BusinessModelType.HYBRID,
            riskLevel = RiskLevel.MEDIUM,
            timeCommitment = TimeCommitment.FLEXIBLE,
            requiredEquipmentIds = listOf("kitchen"),
            equipmentNeeded = listOf("Chafing dishes with fuel burners", "Heavy-duty 50-liter aluminum sufurias", "Serving ladles & tongs", "Insulated hot food transport boxes"),
            supplierAndSourcingGuidance = "Wholesale meat from Dagoretti or Njiru slaughterhouses; rice and spices from Nyamakima.",
            pricingConsiderations = "Charge KSh 500 - 850 per guest plate. An event with 100 guests = KSh 65,000 gross revenue with KSh 30,000 net profit per weekend!",
            marketingStrategy = "Photograph every event spread, share customer video testimonials on TikTok, and partner with local event planners and churches.",
            competitorResearchGuidance = "Portion control and punctuality are everything: serve food exactly on time with warm, generous portions.",
            launchSteps = listOf(
                "Cater a family event or church fellowship at food cost to build photography portfolio.",
                "Acquire 6 stainless steel chafing dishes and serving gear.",
                "Print high-quality laminated event menu packages.",
                "Network with 3 wedding planners and photographers in your area."
            ),
            compliance = listOf(
                ComplianceRequirement("Catering Health Clearance", "County Public Health", "KSh 1,500", "Food production and transport inspection", true),
                ComplianceRequirement("Business Name Registration", "eCitizen BRS", "KSh 950", "Formal business invoice entity", false)
            ),
            mpesaTip = "Require 70% M-PESA deposit upon booking to buy ingredients, balance paid before serving.",
            insiderTips = listOf(
                "Chapati and pilau quality defines your reputation: master soft layered chapatis and authentic coastal pilau.",
                "Always cook for 10% more guests than the client paid for to prevent the nightmare of food running out."
            ),
            risksAndMitigations = listOf(
                "Guest count surprises" to "Include strict guest plate headcount in written agreements.",
                "Transport delays" to "Arrive at the event venue at least 2 hours before scheduled mealtime."
            ),
            lastReviewedDate = "October 2026",
            rating = 4.5,
            reviewCount = 62,
            thumbnailType = "catering"
        ),

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

    // Onboarding State
    fun isOnboardingCompleted(): Boolean {
        return prefs.getBoolean("onboarding_completed", false)
    }

    fun setOnboardingCompleted(completed: Boolean) {
        prefs.edit().putBoolean("onboarding_completed", completed).apply()
    }

    fun getUserProfileName(): String {
        return prefs.getString("user_profile_name", "John Doe") ?: "John Doe"
    }

    fun getUserProfileEmail(): String {
        return prefs.getString("user_profile_email", "john.doe@example.com") ?: "john.doe@example.com"
    }

    fun setUserProfile(name: String, email: String) {
        prefs.edit()
            .putString("user_profile_name", name)
            .putString("user_profile_email", email)
            .apply()
    }

    // Data wipe / privacy clear
    fun clearUserData() {
        prefs.edit().clear().apply()
    }
}
