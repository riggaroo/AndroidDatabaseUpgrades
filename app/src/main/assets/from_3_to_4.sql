ALTER TABLE book_information ADD COLUMN calculated_pages_times_rating INTEGER;
UPDATE book_information SET calculated_pages_times_rating = (book_pages * book_rating) ;