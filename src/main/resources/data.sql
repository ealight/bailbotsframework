INSERT INTO stickers (id, url)
VALUES (1, 'https://blog.jiayu.co/2019/07/telegram-animated-stickers/sticker.webp'),
       (2, 'https://chpic.su/_data/stickers/p/PresidentPutin/PresidentPutin_001.webp'),
       (3, 'https://img-16.stickers.cloud/packs/f02e2ff4-63c9-42d6-9c3d-5c54eb604210/webp/7813c1ec-2384-4e89-a5d5-f18be88d925d.webp')
ON CONFLICT DO NOTHING/UPDATE;