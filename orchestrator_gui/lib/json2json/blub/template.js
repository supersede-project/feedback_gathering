var tmpl;

tmpl = {
  path: '.',
  aggregate: {
    total: function(key, value, existing) {
      if (!isArray(value)) {
        return value;
      } else {
        return value.sort().reverse()[0];
      }
    },
    pages: function(key, value, existing) {
      if (!isArray(value)) {
        return value;
      } else {
        return value.sort().reverse()[0];
      }
    }
  },
  as: {
    bins: {
      path: 'Items.SearchBinSets.SearchBinSet.Bin',
      key: 'BinParameter.Value',
      value: 'BinItemCount',
      aggregate: function(key, value, existing) {
        return Math.max(value, existing || 0);
      }
    },
    items: {
      path: 'Items.Item',
      all: true,
      as: {
        rank: 'SalesRank',
        title: 'ItemAttributes.Title',
        artist: 'ItemAttributes.Artist',
        manufacturer: 'ItemAttributes.Manufacturer',
        category: 'ItemAttributes.ProductGroup',
        price: 'Offers.Offer.OfferListing.Price.FormattedPrice',
        percent_saved: 'Offers.Offer.OfferListing.PercentageSaved',
        availability: 'Offers.Offer.OfferListing.Availability',
        price_new: 'OfferSummary.LowestNewPrice.FormattedPrice',
        price_used: 'OfferSummary.LowestUsedPrice.FormattedPrice',
        url: 'DetailPageURL',
        similar: {
          path: 'SimilarProducts.SimilarProduct',
          key: 'ASIN',
          value: 'Title'
        },
        images: {
          path: '.',
          choose: ['SmallImage', 'MediumImage', 'LargeImage'],
          format: function(node, value, key) {
            return {
              key: key.replace(/Image$/, '').toLowerCase()
            };
          },
          nested: true,
          as: {
            url: 'URL',
            height: 'Height.#',
            width: 'Width.#'
          }
        },
        image_sets: {
          path: 'ImageSets.ImageSet',
          key: '@.Category',
          choose: function(node, value, key) {
            return key !== '@';
          },
          format: function(node, value, key) {
            return {
              key: key.replace(/Image$/, '').toLowerCase()
            };
          },
          nested: true,
          as: {
            url: 'URL',
            height: 'Height.#',
            width: 'Width.#'
          }
        },
        links: {
          path: 'ItemLinks.ItemLink',
          key: 'Description',
          value: 'URL'
        }
      }
    }
  }
};

new ObjectTemplate(tmpl).transform(data);