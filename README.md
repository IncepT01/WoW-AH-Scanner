# World of Warcraft Auction House Analyzer

A Spring Boot application that analyzes auction house data across all World of Warcraft realms and regions using Blizzard’s official API.

The application identifies cross-realm opportunities by comparing item prices, estimating liquidity, and filtering out short-term anomalies using historical data.

---

# Features

- Fetches live auction house data from the official Blizzard API  
- Compares item prices across multiple realms and regions  
- Estimates sell likelihood based on historical sales volume (items sold per day across all realms)  
- Identifies profitable buy/sell arbitrage opportunities  
- Stores 30 days of historical data in JSON format  
- Filters out one-off deals and price spikes using historical trends  

---

# How It Works

1. The application retrieves auction house data for all realms in a region.
2. It aggregates and normalizes item prices.
3. It calculates:
   - Price differences between realms
   - Estimated resale profitability on a target realm
   - Liquidity score based on daily sales volume
4. It ranks items by expected profit and probability of successful resale.
5. Historical data is stored for 30 days to avoid reacting to temporary market fluctuations.

---

# Tech Stack

- Java  
- Spring Boot  
- Blizzard World of Warcraft API  
- JSON-based persistence  
- REST integration  
