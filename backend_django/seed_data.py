import os
import django
import json

os.environ.setdefault('DJANGO_SETTINGS_MODULE', 'biashara_backend.settings')
django.setup()

from api.models import BusinessIdea, Testimonial, Category, UserActivityLog

def run_seed():
    print("Seeding initial categories...")
    categories = [
        ("Agriculture", "agriculture", "#1B5E20"),
        ("Retail & Trade", "retail-trade", "#C62828"),
        ("Food & Beverage", "food-beverage", "#7E22CE"),
        ("Technology", "technology", "#0369A1"),
        ("Services", "services", "#C2410C"),
        ("Manufacturing", "manufacturing", "#1D4ED8")
    ]
    for name, slug, color in categories:
        Category.objects.get_or_create(name=name, defaults={"slug": slug, "color_hex": color})

    print("Seeding initial business ideas...")
    ideas = [
        {
            "idea_id": "urban_farming",
            "title": "Urban Vegetable Farming",
            "category": "Agriculture",
            "summary": "Urban vegetable farming is a profitable business that can be started in small spaces using modern techniques like vertical farming, container gardening, or greenhouse systems.",
            "capital_min_kes": 10000,
            "capital_max_kes": 50000,
            "capital_tier": "Low Capital",
            "estimated_monthly_profit_min": 15000,
            "estimated_monthly_profit_max": 60000,
            "rating": 4.5,
            "review_count": 120,
            "startup_requirements_json": json.dumps([
                "Vertical containers & coco peat seedling trays",
                "Certified high-yield vegetable seeds & organic manure",
                "Drip irrigation tubing & spray nozzles"
            ]),
            "potential_customers_json": json.dumps([
                "Health-conscious estate residents",
                "Local estate mama mbogas and grocery stalls",
                "Neighbourhood restaurants"
            ]),
            "marketing_strategies_json": json.dumps([
                "Share daily growth photos on estate WhatsApp groups",
                "Weekly organic vegetable subscription baskets"
            ]),
            "next_steps_json": json.dumps([
                "Source quality hybrid seeds and seedling trays",
                "Prepare vertical wooden shelves or planter boxes",
                "Launch first harvest pre-orders"
            ])
        },
        {
            "idea_id": "phone_accessories",
            "title": "Mobile Phone Accessories Shop",
            "category": "Retail & Trade",
            "summary": "High-turnover retail shop stocking tempered glass screen protectors, fast chargers, power banks, earphones, phone covers, and smart accessories.",
            "capital_min_kes": 50000,
            "capital_max_kes": 200000,
            "capital_tier": "Medium Capital",
            "estimated_monthly_profit_min": 35000,
            "estimated_monthly_profit_max": 90000,
            "rating": 4.2,
            "review_count": 98,
            "startup_requirements_json": json.dumps(["Display glass counter", "Wholesale stock", "Shop permit"]),
            "potential_customers_json": json.dumps(["Commuters at matatu stops", "Estate smartphone owners"]),
            "marketing_strategies_json": json.dumps(["Free screen cleaning & cable testing"]),
            "next_steps_json": json.dumps(["Secure corner stall", "Purchase wholesale stock from Luthuli Ave"])
        },
        {
            "idea_id": "digital_marketing",
            "title": "Digital Marketing Services",
            "category": "Services",
            "summary": "Help local SMEs, clinics, schools, and salons grow their revenue through Google Business optimization, TikTok campaigns, Meta ads, and social content.",
            "capital_min_kes": 10000,
            "capital_max_kes": 100000,
            "capital_tier": "Low Capital",
            "estimated_monthly_profit_min": 30000,
            "estimated_monthly_profit_max": 95000,
            "rating": 4.4,
            "review_count": 76,
            "startup_requirements_json": json.dumps(["Smartphone or laptop", "Canva Pro", "Wi-Fi"]),
            "potential_customers_json": json.dumps(["Dental clinics", "Pharmacies", "Local salons"]),
            "marketing_strategies_json": json.dumps(["Free Google Maps profile audit"]),
            "next_steps_json": json.dumps(["Pitch 10 local businesses", "Close 3 monthly retainers"])
        },
        {
            "idea_id": "clean_energy",
            "title": "Clean Energy Solutions",
            "category": "Technology",
            "summary": "Sales, installation, and maintenance of affordable solar backup systems, solar water heaters, and rural micro-solar solutions for homes and farms.",
            "capital_min_kes": 100000,
            "capital_max_kes": 350000,
            "capital_tier": "High Growth",
            "estimated_monthly_profit_min": 60000,
            "estimated_monthly_profit_max": 180000,
            "rating": 4.6,
            "review_count": 54,
            "startup_requirements_json": json.dumps(["EPRA Certification", "Multimeter & crimping tools", "Demonstration kit"]),
            "potential_customers_json": json.dumps(["Off-grid homeowners", "Farms needing water pumping", "Barbershops"]),
            "marketing_strategies_json": json.dumps(["Live demo booth outside hardware stores"]),
            "next_steps_json": json.dumps(["Partner with hardware distributors", "Install pilot system"])
        }
    ]

    for item in ideas:
        BusinessIdea.objects.get_or_create(idea_id=item["idea_id"], defaults=item)

    print("Seeding initial testimonials...")
    testimonials = [
        {
            "name": "Brian Mwangi",
            "business_name": "Bamba Prints & Brand KE",
            "county": "Nairobi (Kasarani)",
            "quote": "Started designing flyers with a smartphone. Saved KSh 20,000 and leased a heat-press. Now generating KSh 180,000+ monthly.",
            "starting_capital_kes": 3500,
            "current_monthly_revenue_kes": 180000,
            "advice": "Usione aibu kuanza kidogo. Use the smartphone already in your hand to master one marketable skill."
        },
        {
            "name": "Faith Achieng",
            "business_name": "Kuku Smart Poultry Breeders",
            "county": "Kisumu (Dunga)",
            "quote": "Turned a corner of our compound into a brooder. Started with 30 chicks and KSh 6,500. Now hatch 1,500 chicks monthly.",
            "starting_capital_kes": 6500,
            "current_monthly_revenue_kes": 240000,
            "advice": "Vaccinate religiously and formulate part of your feed locally; margins will surprise you."
        }
    ]

    for t in testimonials:
        Testimonial.objects.get_or_create(name=t["name"], defaults=t)

    print("Seeding sample telemetry tracking logs...")
    sample_logs = [
        {"device_id": "kenya_user_01", "event_type": "COMPLETE_ONBOARDING", "target_title": "Completed Onboarding Wizard", "category": "General"},
        {"device_id": "kenya_user_01", "event_type": "VIEW_IDEA", "target_title": "Urban Vegetable Farming", "category": "Agriculture"},
        {"device_id": "kenya_user_02", "event_type": "SEARCH", "target_title": "food business", "category": "Search"},
        {"device_id": "kenya_user_02", "event_type": "SAVE_IDEA", "target_title": "Fast Food Takeaway", "category": "Food & Beverage"},
        {"device_id": "kenya_user_03", "event_type": "USE_CALCULATOR", "target_title": "Profit Kikokotoo Calculation", "category": "Finance"}
    ]
    for log in sample_logs:
        UserActivityLog.objects.create(**log)

    print("Database seeding completed successfully!")

if __name__ == '__main__':
    run_seed()
