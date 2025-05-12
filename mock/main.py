from selenium import webdriver
from selenium.webdriver.chrome.service import Service
from selenium.webdriver.chrome.options import Options
from selenium.webdriver.common.by import By
from selenium.webdriver.support.ui import WebDriverWait
from selenium.webdriver.support import expected_conditions as EC
import json
from datetime import datetime
import time
import os

def fetch_crypto_news_selenium(max_count=10):
    options = Options()
    options.add_argument("--headless")
    options.add_argument("--disable-gpu")
    options.add_argument("--no-sandbox")

    # 显式指定 chromedriver 路径
    chromedriver_path = os.path.abspath("./chromedriver")  # 你解压后的路径
    service = Service(chromedriver_path)
    driver = webdriver.Chrome(service=service, options=options)

    driver.get("https://cryptopanic.com/")

    # 等待新闻标题元素加载出来
    try:
        # 你可以在这里修改选择器为实际页面上的 class 名称
        WebDriverWait(driver, 15).until(
            EC.presence_of_all_elements_located((By.CSS_SELECTOR, ".news-item__title"))
        )
    except Exception as e:
        print("元素未加载出来，错误信息：", e)
        print(driver.page_source)  # 打印页面源码调试
        driver.quit()
        return []

    news_elements = driver.find_elements(By.CSS_SELECTOR, "news-row news-row-Link")
    print(f"抓到 {len(news_elements)} 条新闻")  # 调试用

    articles = []
    for item in news_elements[:max_count]:
        title = item.text
        link = item.get_attribute("href")
        timestamp = datetime.utcnow().isoformat() + "Z"
        articles.append({
            "title": title,
            "link": link,
            "source": "CryptoPanic",
            "published_at": timestamp,
            "summary": "这是一条模拟的新闻摘要内容。"
        })

    driver.quit()
    return articles