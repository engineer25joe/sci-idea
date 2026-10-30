import sqlite3
from pathlib import Path

from django.db import connections, transaction
from api.models import (
    Category,
    BusinessIdea,
    Testimonial,
    UserActivityLog,
)

SQLITE_DB = Path(__file__).resolve().parent / "db.sqlite3"

MODELS = [
    ("api_category", Category),
    ("api_businessidea", BusinessIdea),
    ("api_testimonial", Testimonial),
    ("api_useractivitylog", UserActivityLog),
]


def main():
    if not SQLITE_DB.exists():
        raise FileNotFoundError(f"SQLite database not found: {SQLITE_DB}")

    print("\nSCI IDEA: SQLITE TO NEON DATA MIGRATION")
    print("=" * 55)

    source = sqlite3.connect(str(SQLITE_DB))
    source.row_factory = sqlite3.Row

    try:
        with transaction.atomic(using="default"):
            for table_name, model in MODELS:
                cursor = source.cursor()
                cursor.execute(f'SELECT * FROM "{table_name}"')
                rows = cursor.fetchall()

                print(f"\nMigrating {table_name}: {len(rows)} records")

                field_names = {
                    field.column: field.name
                    for field in model._meta.concrete_fields
                }

                migrated = 0
                skipped = 0

                for row in rows:
                    values = {}

                    for column in row.keys():
                        if column in field_names:
                            values[field_names[column]] = row[column]

                    pk = values.get("id")

                    if model.objects.using("default").filter(pk=pk).exists():
                        skipped += 1
                        continue

                    model.objects.using("default").create(**values)
                    migrated += 1

                print(f"  Imported: {migrated}")
                print(f"  Already present/skipped: {skipped}")

        print("\nMIGRATION TRANSACTION COMPLETED SUCCESSFULLY")

    finally:
        source.close()

    print("\nFINAL NEON DATABASE COUNTS")
    print("-" * 55)

    for _, model in MODELS:
        count = model.objects.using("default").count()
        print(f"{model.__name__}: {count}")

    connections["default"].close()


if __name__ == "__main__":
    main()