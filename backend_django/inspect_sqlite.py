import sqlite3
from pathlib import Path

DB_PATH = Path(__file__).resolve().parent / "db.sqlite3"

print("\nSCI IDEA - SQLITE DATABASE INSPECTION")
print("=" * 50)

if not DB_PATH.exists():
    print("ERROR: db.sqlite3 was not found.")
    raise SystemExit(1)

print(f"Database: {DB_PATH}")
print(f"File size: {DB_PATH.stat().st_size:,} bytes")

try:
    connection = sqlite3.connect(str(DB_PATH))
    cursor = connection.cursor()

    cursor.execute("""
        SELECT name
        FROM sqlite_master
        WHERE type = 'table'
        ORDER BY name;
    """)

    tables = cursor.fetchall()

    print(f"\nTables found: {len(tables)}")
    print("\nEXISTING RECORD COUNTS")
    print("-" * 50)

    for (table_name,) in tables:
        if not table_name.startswith("sqlite_"):
            cursor.execute(f'SELECT COUNT(*) FROM "{table_name}"')
            count = cursor.fetchone()[0]
            print(f"{table_name}: {count}")

    print("\nINSPECTION COMPLETED SUCCESSFULLY")

except sqlite3.Error as error:
    print(f"DATABASE ERROR: {error}")

finally:
    if "connection" in locals():
        connection.close()